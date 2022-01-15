package com.github.jakubtomekcz.doctorscheduler.model;

import com.google.common.collect.ImmutableMap;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class ScheduleSummary {

    private final ImmutableMap<Person, Integer> weekDayTotals;
    private final ImmutableMap<Person, Integer> weekendDayTotals;
    private final ImmutableMap<Person, Integer> allDayTotals;

    public static ScheduleSummary of(PreferenceTable preferenceTable, Schedule schedule) {

        ImmutableMap.Builder<Person, Integer> weekDayTotalsBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<Person, Integer> weekendDayTotalsBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<Person, Integer> allDayTotalsBuilder = ImmutableMap.builder();

        for (Person person : preferenceTable.getPersons()) {
            int weekDayCount = schedule.getWeekShiftDaysCountForPerson(person);
            int weekendDayCount = schedule.getWeekendShiftDaysCountForPerson(person);
            int allDayCount = schedule.getShiftDaysCountForPerson(person);

            weekDayTotalsBuilder.put(person, weekDayCount);
            weekendDayTotalsBuilder.put(person, weekendDayCount);
            allDayTotalsBuilder.put(person, allDayCount);
        }

        return ScheduleSummary.builder()
                .allDayTotals(allDayTotalsBuilder.build())
                .weekDayTotals(weekDayTotalsBuilder.build())
                .weekendDayTotals(weekendDayTotalsBuilder.build())
                .build();
    }
}
