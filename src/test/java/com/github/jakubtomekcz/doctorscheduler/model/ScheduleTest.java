package com.github.jakubtomekcz.doctorscheduler.model;


import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.CARL;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.HOMER;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.LENNY;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.dDayPlusNDays;
import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.YES;
import static com.github.jakubtomekcz.doctorscheduler.model.Person.person;
import static org.assertj.core.api.Assertions.assertThat;

class ScheduleTest {

    @Test
    void getShiftDaysCountForPerson() {
        PreferenceTable.Builder builder = PreferenceTable.builder();
        for (int i = 1; i <= 8; i++) {
            builder.put(LENNY, dDayPlusNDays(i), YES);
            builder.put(CARL, dDayPlusNDays(i), YES);
            builder.put(HOMER, dDayPlusNDays(i), YES);
        }
        Schedule schedule = ScheduleBuilder.forPreferenceTable(builder.build())
                .put(dDayPlusNDays(1), LENNY)
                .put(dDayPlusNDays(2), CARL)
                .put(dDayPlusNDays(3), HOMER)
                .put(dDayPlusNDays(4), LENNY)
                .put(dDayPlusNDays(5), CARL)
                .put(dDayPlusNDays(6), HOMER)
                .put(dDayPlusNDays(7), LENNY)
                .put(dDayPlusNDays(8), CARL)
                .build();

        SoftAssertions softly = new SoftAssertions();
        assertThat(schedule.getShiftDaysCountForPerson(LENNY)).isEqualTo(3);
        assertThat(schedule.getShiftDaysCountForPerson(CARL)).isEqualTo(3);
        assertThat(schedule.getShiftDaysCountForPerson(HOMER)).isEqualTo(2);
        assertThat(schedule.getShiftDaysCountForPerson(person("Marge"))).isEqualTo(0);
        softly.assertAll();
    }

    @Test
    void getWeekendShiftDaysCountForPerson() {
        PreferenceTable.Builder builder = PreferenceTable.builder();
        for (int i = 1; i <= 8; i++) {
            builder.put(LENNY, dDayPlusNDays(i), YES);
            builder.put(CARL, dDayPlusNDays(i), YES);
            builder.put(HOMER, dDayPlusNDays(i), YES);
        }
        Schedule schedule = ScheduleBuilder.forPreferenceTable(builder.build())
                .put(dDayPlusNDays(1), LENNY)
                .put(dDayPlusNDays(2), CARL)
                .put(dDayPlusNDays(3), HOMER)
                .put(dDayPlusNDays(4), LENNY)
                .put(dDayPlusNDays(5), CARL)
                .put(dDayPlusNDays(6), HOMER)
                .put(dDayPlusNDays(7), LENNY)
                .put(dDayPlusNDays(8), CARL)
                .build();

        SoftAssertions softly = new SoftAssertions();
        assertThat(schedule.getWeekendShiftDaysCountForPerson(LENNY)).isEqualTo(0);
        assertThat(schedule.getWeekendShiftDaysCountForPerson(CARL)).isEqualTo(1);
        assertThat(schedule.getWeekendShiftDaysCountForPerson(HOMER)).isEqualTo(1);
        assertThat(schedule.getWeekendShiftDaysCountForPerson(person("Marge"))).isEqualTo(0);
        softly.assertAll();
    }

    @Test
    void getWeekShiftDaysCountForPerson() {
        PreferenceTable.Builder builder = PreferenceTable.builder();
        for (int i = 1; i <= 8; i++) {
            builder.put(LENNY, dDayPlusNDays(i), YES);
            builder.put(CARL, dDayPlusNDays(i), YES);
            builder.put(HOMER, dDayPlusNDays(i), YES);
        }
        Schedule schedule = ScheduleBuilder.forPreferenceTable(builder.build())
                .put(dDayPlusNDays(1), LENNY)
                .put(dDayPlusNDays(2), CARL)
                .put(dDayPlusNDays(3), HOMER)
                .put(dDayPlusNDays(4), LENNY)
                .put(dDayPlusNDays(5), CARL)
                .put(dDayPlusNDays(6), HOMER)
                .put(dDayPlusNDays(7), LENNY)
                .put(dDayPlusNDays(8), CARL)
                .build();

        SoftAssertions softly = new SoftAssertions();
        assertThat(schedule.getWeekShiftDaysCountForPerson(LENNY)).isEqualTo(3);
        assertThat(schedule.getWeekShiftDaysCountForPerson(CARL)).isEqualTo(2);
        assertThat(schedule.getWeekShiftDaysCountForPerson(HOMER)).isEqualTo(1);
        assertThat(schedule.getWeekShiftDaysCountForPerson(person("Marge"))).isEqualTo(0);
        softly.assertAll();
    }
}
