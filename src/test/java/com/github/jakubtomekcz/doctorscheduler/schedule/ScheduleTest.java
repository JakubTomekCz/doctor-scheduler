package com.github.jakubtomekcz.doctorscheduler.schedule;


import org.junit.jupiter.api.Test;

import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.YES;
import static org.assertj.core.api.Assertions.assertThat;

class ScheduleTest {

    @Test
    void getServiceDaysCountForPerson() {
        PreferenceTable.Builder builder = PreferenceTable.builder();
        for (int i = 1; i <= 8; i++) {
            builder.put("Lenny", String.valueOf(i), YES);
        }
        Schedule schedule = ScheduleBuilder.forPreferenceTable(builder.build())
                .put("1", "Lenny")
                .put("2", "Carl")
                .put("3", "Lenny")
                .put("4", "Carl")
                .put("5", "Homer")
                .put("6", "Carl")
                .put("7", "Lenny")
                .put("8", "Carl")
                .build();
        assertThat(schedule).satisfies(s -> {
            assertThat(s.getServiceDaysCountForPerson("Lenny")).isEqualTo(3);
            assertThat(s.getServiceDaysCountForPerson("Carl")).isEqualTo(4);
            assertThat(s.getServiceDaysCountForPerson("Homer")).isEqualTo(1);
            assertThat(s.getServiceDaysCountForPerson("Marge")).isEqualTo(0);
        });
    }
}
