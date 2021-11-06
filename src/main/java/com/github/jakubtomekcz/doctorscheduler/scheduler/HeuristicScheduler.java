package com.github.jakubtomekcz.doctorscheduler.scheduler;

import com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType;
import com.github.jakubtomekcz.doctorscheduler.error.UiMessageException;
import com.github.jakubtomekcz.doctorscheduler.model.Date;
import com.github.jakubtomekcz.doctorscheduler.model.Person;
import com.github.jakubtomekcz.doctorscheduler.model.PreferenceTable;
import com.github.jakubtomekcz.doctorscheduler.model.Schedule;
import com.github.jakubtomekcz.doctorscheduler.model.ScheduleBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.PREFER;
import static com.github.jakubtomekcz.doctorscheduler.error.UiMessageException.MessageCode.CANNOT_BUILD_SCHEDULE;
import static java.lang.String.format;

/**
 * Creates a schedule based on given preferences.
 * Attempts to maximize satisfaction.
 */
@Slf4j
@RequiredArgsConstructor
public class HeuristicScheduler implements Scheduler {

    private final int maxIterations;
    private int iterationCounter = 0;

    @Override
    public Schedule createSchedule(PreferenceTable preferenceTable) {
        ScheduleBuilder builder = ScheduleBuilder.forPreferenceTable(preferenceTable);

        return findSolutionFor(builder)
                .map(ScheduleBuilder::build)
                .orElseThrow(() -> new UiMessageException(CANNOT_BUILD_SCHEDULE));
    }

    private Optional<ScheduleBuilder> findSolutionFor(ScheduleBuilder builder) {
        iterationCounter++;
        log.debug("Starting iteration #" + iterationCounter);
        if (iterationCounter > maxIterations) {
            log.debug(format("Max limit of %d iterations exceeded. Giving up. Sorry.", maxIterations));
            return Optional.empty();
        } else if (builder.isComplete()) {
            log.debug(format("Hurray! A solution was found after mere %d iterations!", iterationCounter));
            return Optional.of(builder);
        } else if (builder.getAssignablePersons().values().stream().anyMatch(Set::isEmpty)) {
            log.debug("Nope, this setup is a dead end.");
            return Optional.empty();
        }
        Date date = selectDateToBeAssignedAPerson(builder);
        log.debug(format("The next date to be assigned is %s", date));
        List<Person> sortedCandidates = builder.getAssignablePersons().get(date).stream()
                .sorted(HeuristicCandidatePersonComparator.forScheduleAndDate(builder, date))
                .toList();
        log.debug(format("The candidates for date %s will come in this order: %s", date, sortedCandidates));
        for (Person person : sortedCandidates) {
            log.debug(format("Trying candidate %s for the date %s", person, date));
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
    private Date selectDateToBeAssignedAPerson(ScheduleBuilder builder) {

        Date bestCandidatePreferredByFewestPeople = null;
        int fewestNumberOfPeoplePreferringTheBestCandidate = Integer.MAX_VALUE;
        Date bestCandidateWithFewestAssignablePeople = null;
        int fewestNumberOfAssignablePeople = Integer.MAX_VALUE;

        for (Map.Entry<Date, Set<Person>> entry : builder.getAssignablePersons().entrySet()) {
            Date date = entry.getKey();
            Set<Person> assignablePersons = entry.getValue();

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
