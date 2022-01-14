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

        var weekDayTotalsBuilder = ImmutableMap.builder();
        var weekendDayTotalsBuilder = ImmutableMap.builder();
        var allDayTotalsBuilder = ImmutableMap.builder();

        for (Person person : preferenceTable.getPersons()) {
            // TODO
            int weekDayCounter = 0;
            int weekendDayCounter = 0;
            int allDayCounter = schedule.getShiftDaysCountForPerson(person);
        }

        return null;
    }
}
