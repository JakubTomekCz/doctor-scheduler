package com.github.jakubtomekcz.doctorscheduler.model;


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
        assertThat(schedule).satisfies(s -> {
            assertThat(s.getShiftDaysCountForPerson(LENNY)).isEqualTo(3);
            assertThat(s.getShiftDaysCountForPerson(CARL)).isEqualTo(3);
            assertThat(s.getShiftDaysCountForPerson(HOMER)).isEqualTo(2);
            assertThat(s.getShiftDaysCountForPerson(person("Marge"))).isEqualTo(0);
        });
    }
}
