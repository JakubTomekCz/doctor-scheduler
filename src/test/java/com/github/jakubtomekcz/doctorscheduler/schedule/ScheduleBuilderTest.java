package com.github.jakubtomekcz.doctorscheduler.schedule;

import com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScheduleBuilderTest {

    @Test
    void build() {
        PreferenceTable table = PreferenceTable.builder()
                .put("Lenny", "1", PreferenceType.YES)
                .put("Carl", "1", PreferenceType.YES)
                .put("Lenny", "2", PreferenceType.YES)
                .put("Carl", "2", PreferenceType.YES)
                .build();
        ScheduleBuilder.forPreferenceTable(table)
                .put("1", "Lenny")
                .put("2", "Carl")
                .build();
    }

    @Test
    void cannotBuildIncompleteSchedule() {
        PreferenceTable table = PreferenceTable.builder()
                .put("Lenny", "1", PreferenceType.YES)
                .put("Carl", "1", PreferenceType.YES)
                .put("Lenny", "2", PreferenceType.YES)
                .put("Carl", "2", PreferenceType.YES)
                .build();
        assertThatThrownBy(() -> ScheduleBuilder.forPreferenceTable(table).build())
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void isValid() {
        PreferenceTable table = PreferenceTable.builder()
                .put("Lenny", "1", PreferenceType.YES)
                .put("Carl", "1", PreferenceType.YES)
                .put("Lenny", "2", PreferenceType.YES)
                .put("Carl", "2", PreferenceType.YES)
                .build();
        ScheduleBuilder builder = ScheduleBuilder.forPreferenceTable(table)
                .put("1", "Lenny")
                .put("2", "Carl");
        assertThat(builder.isValid()).isTrue();
    }

    @Test
    void isNotValidDisrespectsRefusal() {
        PreferenceTable table = PreferenceTable.builder()
                .put("Lenny", "1", PreferenceType.YES)
                .put("Carl", "1", PreferenceType.YES)
                .put("Lenny", "2", PreferenceType.YES)
                .put("Carl", "2", PreferenceType.NO)
                .build();
        ScheduleBuilder builder = ScheduleBuilder.forPreferenceTable(table)
                .put("1", "Lenny")
                .put("2", "Carl");
        assertThat(builder.isValid()).isFalse();
    }

    @Test
    void isNotValidViolatesTwoDaysRest() {
        PreferenceTable table = PreferenceTable.builder()
                .put("Lenny", "1", PreferenceType.YES)
                .put("Carl", "1", PreferenceType.YES)
                .put("Lenny", "2", PreferenceType.YES)
                .put("Carl", "2", PreferenceType.YES)
                .build();
        ScheduleBuilder builder = ScheduleBuilder.forPreferenceTable(table)
                .put("1", "Lenny")
                .put("2", "Lenny");
        assertThat(builder.isValid()).isTrue();
    }

    @Test
    void cannotBuildInvalidSchedule() {
        PreferenceTable table = PreferenceTable.builder()
                .put("Lenny", "1", PreferenceType.YES)
                .put("Carl", "1", PreferenceType.YES)
                .put("Lenny", "2", PreferenceType.YES)
                .put("Carl", "2", PreferenceType.YES)
                .build();
        ScheduleBuilder builder = ScheduleBuilder.forPreferenceTable(table)
                .put("1", "Lenny")
                .put("2", "Lenny");
        assertThatThrownBy(builder::build)
                .isInstanceOf(IllegalStateException.class);
    }
}
