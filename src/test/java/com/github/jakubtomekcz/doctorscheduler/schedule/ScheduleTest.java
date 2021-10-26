package com.github.jakubtomekcz.doctorscheduler.schedule;


import com.github.jakubtomekcz.doctorscheduler.model.Person;
import com.github.jakubtomekcz.doctorscheduler.model.PreferenceTable;
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
                .put(date("3"), LENNY)
                .put(date("4"), CARL)
                .put(date("5"), HOMER)
                .put(date("6"), CARL)
                .put(date("7"), LENNY)
                .put(date("8"), CARL)
                .build();
        assertThat(schedule).satisfies(s -> {
            assertThat(s.getServiceDaysCountForPerson(LENNY)).isEqualTo(3);
            assertThat(s.getServiceDaysCountForPerson(CARL)).isEqualTo(4);
            assertThat(s.getServiceDaysCountForPerson(HOMER)).isEqualTo(1);
            assertThat(s.getServiceDaysCountForPerson(person("Marge"))).isEqualTo(0);
        });
    }
}
