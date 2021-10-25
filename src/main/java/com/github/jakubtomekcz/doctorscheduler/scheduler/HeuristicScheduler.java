package com.github.jakubtomekcz.doctorscheduler.scheduler;

import com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType;
import com.github.jakubtomekcz.doctorscheduler.error.UiMessageException;
import com.github.jakubtomekcz.doctorscheduler.schedule.PreferenceTable;
import com.github.jakubtomekcz.doctorscheduler.schedule.Schedule;
import com.github.jakubtomekcz.doctorscheduler.schedule.ScheduleBuilder;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.PREFER;
import static com.github.jakubtomekcz.doctorscheduler.error.UiMessageException.MessageCode.CANNOT_BUILD_SCHEDULE;

/**
 * Creates a schedule based on given preferences.
 * Attempts to maximize satisfaction.
 */
public class HeuristicScheduler implements Scheduler {

    @Override
    public Schedule createSchedule(PreferenceTable preferenceTable) {
        ScheduleBuilder builder = ScheduleBuilder.forPreferenceTable(preferenceTable);

        return findSolutionFor(builder)
                .map(ScheduleBuilder::build)
                .orElseThrow(() -> new UiMessageException(CANNOT_BUILD_SCHEDULE));
    }

    private Optional<ScheduleBuilder> findSolutionFor(ScheduleBuilder builder) {
        if (builder.isComplete()) {
            return Optional.of(builder);
        } else if (builder.getAssignablePersons().values().stream().anyMatch(Set::isEmpty)) {
            return Optional.empty();
        }
        String date = selectDateToBeAssignedAPerson(builder);
        List<String> sortedCandidates = sortCandidates(builder.getAssignablePersons().get(date));
        for (String person : sortedCandidates) {
            ScheduleBuilder extendedBuilder = builder.copy().put(date, person);
            Optional<ScheduleBuilder> solution = findSolutionFor(extendedBuilder);
            if (solution.isPresent()) {
                return solution;
            }
        }
        return Optional.empty();
    }

    /**
     * Heuristic method of selecting the next date to be assigned a person
     * Priorities:
     * 1. Date with only one possible person
     * 2. Date where at least one possible person has {@link PreferenceType#PREFER} (prefer fewer)
     * 3. Other dates, prefer fewer possible persons
     */
    private String selectDateToBeAssignedAPerson(ScheduleBuilder builder) {

        String bestCandidatePreferredByFewestPeople = null;
        int fewestNumberOfPeoplePreferringTheBestCandidate = Integer.MAX_VALUE;
        String bestCandidateWithFewestAssignablePeople = null;
        int fewestNumberOfAssignablePeople = Integer.MAX_VALUE;

        for (Map.Entry<String, Set<String>> entry : builder.getAssignablePersons().entrySet()) {
            String date = entry.getKey();
            Set<String> assignablePersons = entry.getValue();

            if (assignablePersons.size() == 1) {
                return date;
            }

            int numberOfAssignablePeopleThatPreferThisDay = Math.toIntExact(assignablePersons.stream()
                    .filter(person -> builder.getPreferenceTable().getPreference(person, date) == PREFER)
                    .count());
            if (numberOfAssignablePeopleThatPreferThisDay > 0
                    && numberOfAssignablePeopleThatPreferThisDay < fewestNumberOfPeoplePreferringTheBestCandidate) {
                fewestNumberOfPeoplePreferringTheBestCandidate = numberOfAssignablePeopleThatPreferThisDay;
                bestCandidatePreferredByFewestPeople = date;
            } else if (assignablePersons.size() < fewestNumberOfAssignablePeople) {
                fewestNumberOfAssignablePeople = assignablePersons.size();
                bestCandidateWithFewestAssignablePeople = date;
            }
        }

        return Stream.of(bestCandidatePreferredByFewestPeople, bestCandidateWithFewestAssignablePeople)
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Failed to find the next date to be assigned a person."));
    }
}
