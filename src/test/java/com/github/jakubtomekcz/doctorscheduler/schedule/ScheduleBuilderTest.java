package com.github.jakubtomekcz.doctorscheduler.schedule;

import com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScheduleBuilderTest {

    @Test
    void build() {
        PreferenceTable table = PreferenceTable.builder()
                .put("Lenny", "1", PreferenceType.YES)
                .put("Lenny", "2", PreferenceType.YES)
                .build();
        ScheduleBuilder.forPreferenceTable(table)
                .put("1", "Lenny")
                .put("2", "Lenny")
                .build();
    }

    @Test
    void incompleteSchedule() {
        PreferenceTable table = PreferenceTable.builder()
                .put("Lenny", "1", PreferenceType.YES)
                .put("Lenny", "2", PreferenceType.YES)
                .build();
        assertThatThrownBy(() -> ScheduleBuilder.forPreferenceTable(table).build())
                .isInstanceOf(NullPointerException.class);
    }
}
