package com.github.jakubtomekcz.doctorscheduler.schedule;

import com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.NO;
import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.PREFER;
import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.YES;
import static java.lang.String.format;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

@Getter
public class ScheduleBuilder {

    private final PreferenceTable preferenceTable;

    /**
     * date -> person
     */
    private final Map<String, String> schedule;

    /**
     * date -> Set of persons that can be assigned without violating requirements
     */
    private final Map<String, Set<String>> assignablePersons;

    public static ScheduleBuilder forPreferenceTable(PreferenceTable preferenceTable) {
        return new ScheduleBuilder(preferenceTable);
    }

    private ScheduleBuilder(PreferenceTable preferenceTable) {
        this.preferenceTable = preferenceTable;
        this.schedule = new HashMap<>();
        assignablePersons = preferenceTable.getDates().stream()
                .collect(toMap(date -> date, date -> assignablePersonsForDate(preferenceTable, date)));
    }

    private ScheduleBuilder(PreferenceTable preferenceTable,
                            Map<String, String> schedule,
                            Map<String, Set<String>> assignablePersons) {
        this.preferenceTable = preferenceTable;
        this.schedule = schedule;
        this.assignablePersons = assignablePersons;
    }

    public ScheduleBuilder put(String date, String person) {
        schedule.put(date, person);
        reduceAssignablePersons(date, person);
        return this;
    }

    public SatisfactionCriteria criteria() {
        return SatisfactionCriteria.of(this);
    }

    public Schedule build() {
        if (!isConsistent()) {
            throw new IllegalStateException("Cannot build schedule. Elementary schedule requirements are not met.");
        }
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        preferenceTable.getDates().forEach(date -> {
            if (schedule.get(date) == null) {
                throw new NullPointerException(format("Cannot build Schedule. Missing person for date %s", date));
            }
            builder.put(date, schedule.get(date));
        });
        return new Schedule(builder.build());
    }

    /**
     * @return {@code true} if each date has assigned a person
     */
    public boolean isComplete() {
        return preferenceTable.getDates().stream()
                .map(schedule::get)
                .allMatch(Objects::nonNull);
    }

    public ScheduleBuilder copy() {
        Map<String, String> scheduleClone = new HashMap<>(schedule);
        Map<String, Set<String>> assignablePersonsClone = assignablePersons.entrySet().stream()
                .collect(toMap(Map.Entry::getKey, entry -> new HashSet<>(entry.getValue())));
        return new ScheduleBuilder(preferenceTable, scheduleClone, assignablePersonsClone);
    }

    /**
     * Checks if the current schedule satisfies the elementary requirements:
     * <p>
     * 1. Refusal of service indicated by the {@link PreferenceType#NO} preference must be respected
     * 2. Each person must have at least two days rest between two service days
     *
     * @return {@code true} if the requirements above are satisfied
     */
    public boolean isConsistent() {
        return isRefusalOfServiceRespected() && isThereAlwaysTwoDaysRestAfterService();
    }

    private boolean isRefusalOfServiceRespected() {
        return schedule.entrySet().stream()
                .noneMatch(entry -> preferenceTable.getPreference(entry.getValue(), entry.getKey()) == NO);
    }

    private boolean isThereAlwaysTwoDaysRestAfterService() {
        for (int dateIndex = 0; dateIndex < preferenceTable.getDates().size() - 1; dateIndex++) {
            if (isSamePersonScheduledOnDayIndexes(dateIndex, dateIndex + 1)
                    || dateIndex < preferenceTable.getDates().size() - 2
                    && isSamePersonScheduledOnDayIndexes(dateIndex, dateIndex + 2)) {
                return false;
            }
        }
        return true;
    }

    private boolean isSamePersonScheduledOnDayIndexes(int dateIndex1, int dateIndex2) {
        List<String> dates = preferenceTable.getDates();
        return schedule.containsKey(dates.get(dateIndex1))
                && schedule.containsKey(dates.get(dateIndex2))
                && schedule.get(dates.get(dateIndex1)).equals(schedule.get(dates.get(dateIndex2)));
    }

    private Set<String> assignablePersonsForDate(PreferenceTable preferenceTable, String date) {
        return preferenceTable.getPersons().stream()
                .filter(person -> preferenceTable.getPreference(person, date) == YES
                        || preferenceTable.getPreference(person, date) == PREFER)
                .collect(toSet());
    }

    private void reduceAssignablePersons(String assignedDate, String assignedPerson) {
        assignablePersons.remove(assignedDate);
        List<String> dates = preferenceTable.getDates();
        int assignedDateIdx = dates.indexOf(assignedDate);
        for (int i : List.of(assignedDateIdx - 2, assignedDateIdx - 1, assignedDateIdx + 1, assignedDateIdx + 2)) {
            if (i >= 0 && i < dates.size()) {
                String day = dates.get(i);
                if (assignablePersons.containsKey(day)) {
                    assignablePersons.get(day).remove(assignedPerson);
                }
            }
        }
    }
}
