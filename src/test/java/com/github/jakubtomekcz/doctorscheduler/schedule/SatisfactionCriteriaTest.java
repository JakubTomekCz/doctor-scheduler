package com.github.jakubtomekcz.doctorscheduler.schedule;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


class SatisfactionCriteriaTest {

    @Test
    void compare() {
        PreferenceTable table = preferenceTable();
        SatisfactionCriteria alfa = ScheduleBuilder.forPreferenceTable(table)
                .put("1", "Lenny")
                .criteria();

        List<SatisfactionCriteria> sorted = Stream.of(alfa)
                .sorted()
                .toList();

        assertThat(sorted).containsExactly(alfa);
    }

    private PreferenceTable preferenceTable() {
        return PreferenceTable.builder().build();
    }
}
