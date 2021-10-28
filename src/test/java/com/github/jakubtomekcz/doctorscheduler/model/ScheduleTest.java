package com.github.jakubtomekcz.doctorscheduler.model;


import org.junit.jupiter.api.Test;

import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.YES;
import static com.github.jakubtomekcz.doctorscheduler.model.Date.date;
import static com.github.jakubtomekcz.doctorscheduler.model.Person.person;
import static org.assertj.core.api.Assertions.assertThat;

class ScheduleTest {

    private static final Person LENNY = new Person("Lenny");
    private static final Person CARL = new Person("Carl");
    private static final Person HOMER = new Person("Homer");

    @Test
    void getServiceDaysCountForPerson() {
        PreferenceTable.Builder builder = PreferenceTable.builder();
        for (int i = 1; i <= 8; i++) {
            builder.put(LENNY, date(String.valueOf(i)), YES);
        }
        Schedule schedule = ScheduleBuilder.forPreferenceTable(builder.build())
                .put(date("1"), LENNY)
                .put(date("2"), CARL)
                .put(date("3"), HOMER)
                .put(date("4"), LENNY)
                .put(date("5"), CARL)
                .put(date("6"), HOMER)
                .put(date("7"), LENNY)
                .put(date("8"), CARL)
                .build();
        assertThat(schedule).satisfies(s -> {
            assertThat(s.getServiceDaysCountForPerson(LENNY)).isEqualTo(3);
            assertThat(s.getServiceDaysCountForPerson(CARL)).isEqualTo(3);
            assertThat(s.getServiceDaysCountForPerson(HOMER)).isEqualTo(2);
            assertThat(s.getServiceDaysCountForPerson(person("Marge"))).isEqualTo(0);
        });
    }
}
