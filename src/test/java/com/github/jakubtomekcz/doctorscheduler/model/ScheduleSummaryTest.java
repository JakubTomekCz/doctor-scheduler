package com.github.jakubtomekcz.doctorscheduler.model;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.CARL;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.HOMER;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.LENNY;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.dDayPlusNDays;
import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.YES;
import static org.assertj.core.api.Assertions.assertThat;

class ScheduleSummaryTest {

    @Test
    void getWeekDayTotals() {
        PreferenceTable preferenceTable = mockPreferenceTable();
        Schedule schedule = ScheduleBuilder.forPreferenceTable(preferenceTable)
                .put(dDayPlusNDays(1), LENNY)
                .put(dDayPlusNDays(2), CARL)
                .put(dDayPlusNDays(3), HOMER)
                .put(dDayPlusNDays(4), LENNY)
                .put(dDayPlusNDays(5), CARL)
                .put(dDayPlusNDays(6), HOMER)
                .put(dDayPlusNDays(7), LENNY)
                .put(dDayPlusNDays(8), CARL)
                .build();

        ScheduleSummary scheduleSummary = ScheduleSummary.of(preferenceTable, schedule);

        assertThat(scheduleSummary.getWeekDayTotals())
                .isEqualTo(ImmutableMap.of(
                        LENNY, 3,
                        CARL, 2,
                        HOMER, 1));
    }

    @Test
    void getWeekendDayTotals() {
        PreferenceTable preferenceTable = mockPreferenceTable();
        Schedule schedule = ScheduleBuilder.forPreferenceTable(preferenceTable)
                .put(dDayPlusNDays(1), LENNY)
                .put(dDayPlusNDays(2), CARL)
                .put(dDayPlusNDays(3), HOMER)
                .put(dDayPlusNDays(4), LENNY)
                .put(dDayPlusNDays(5), CARL)
                .put(dDayPlusNDays(6), HOMER)
                .put(dDayPlusNDays(7), LENNY)
                .put(dDayPlusNDays(8), CARL)
                .build();

        ScheduleSummary scheduleSummary = ScheduleSummary.of(preferenceTable, schedule);

        assertThat(scheduleSummary.getWeekendDayTotals())
                .isEqualTo(ImmutableMap.of(
                        LENNY, 0,
                        CARL, 1,
                        HOMER, 1));
    }

    @Test
    void getAllDayTotals() {
        PreferenceTable preferenceTable = mockPreferenceTable();
        Schedule schedule = ScheduleBuilder.forPreferenceTable(preferenceTable)
                .put(dDayPlusNDays(1), LENNY)
                .put(dDayPlusNDays(2), CARL)
                .put(dDayPlusNDays(3), HOMER)
                .put(dDayPlusNDays(4), LENNY)
                .put(dDayPlusNDays(5), CARL)
                .put(dDayPlusNDays(6), HOMER)
                .put(dDayPlusNDays(7), LENNY)
                .put(dDayPlusNDays(8), CARL)
                .build();

        ScheduleSummary scheduleSummary = ScheduleSummary.of(preferenceTable, schedule);

        assertThat(scheduleSummary.getAllDayTotals())
                .isEqualTo(ImmutableMap.of(
                        LENNY, 3,
                        CARL, 3,
                        HOMER, 2));
    }

    private PreferenceTable mockPreferenceTable() {
        PreferenceTable.Builder builder = PreferenceTable.builder();
        for (int i = 1; i <= 8; i++) {
            builder.put(LENNY, dDayPlusNDays(i), YES);
            builder.put(CARL, dDayPlusNDays(i), YES);
            builder.put(HOMER, dDayPlusNDays(i), YES);
        }
        return builder.build();
    }
}
