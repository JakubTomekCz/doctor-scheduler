package com.github.jakubtomekcz.doctorscheduler.model;


import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class ScheduleTest {

    @Test
    void getServiceDaysCountForPerson() {
        List<String> dates = IntStream.rangeClosed(1, 8)
                .boxed()
                .map(String::valueOf)
                .toList();
        Schedule schedule = Schedule.builderForDates(dates)
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
