package com.github.jakubtomekcz.doctorscheduler.schedule;

import com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

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
    void isConsistent() {
        PreferenceTable table = PreferenceTable.builder()
                .put("Lenny", "1", PreferenceType.YES)
                .put("Carl", "1", PreferenceType.YES)
                .put("Lenny", "2", PreferenceType.YES)
                .put("Carl", "2", PreferenceType.YES)
                .build();
        ScheduleBuilder builder = ScheduleBuilder.forPreferenceTable(table)
                .put("1", "Lenny")
                .put("2", "Carl");
        assertThat(builder.isConsistent()).isTrue();
    }

    @Test
    void isNotConsistentDisrespectsRefusal() {
        PreferenceTable table = PreferenceTable.builder()
                .put("Lenny", "1", PreferenceType.YES)
                .put("Carl", "1", PreferenceType.YES)
                .put("Lenny", "2", PreferenceType.YES)
                .put("Carl", "2", PreferenceType.NO)
                .build();
        ScheduleBuilder builder = ScheduleBuilder.forPreferenceTable(table)
                .put("1", "Lenny")
                .put("2", "Carl");
        assertThat(builder.isConsistent()).isFalse();
    }

    @Test
    void isNotConsistentViolatesTwoDaysRest() {
        PreferenceTable table = PreferenceTable.builder()
                .put("Lenny", "1", PreferenceType.YES)
                .put("Carl", "1", PreferenceType.YES)
                .put("Lenny", "2", PreferenceType.YES)
                .put("Carl", "2", PreferenceType.YES)
                .build();
        ScheduleBuilder builder = ScheduleBuilder.forPreferenceTable(table)
                .put("1", "Lenny")
                .put("2", "Lenny");
        assertThat(builder.isConsistent()).isFalse();
    }

    @Test
    void cannotBuildInconsistentSchedule() {
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

    @Test
    void isComplete() {
        PreferenceTable table = PreferenceTable.builder()
                .put("Lenny", "1", PreferenceType.YES)
                .put("Carl", "1", PreferenceType.YES)
                .put("Lenny", "2", PreferenceType.YES)
                .put("Carl", "2", PreferenceType.YES)
                .build();
        ScheduleBuilder builder = ScheduleBuilder.forPreferenceTable(table)
                .put("1", "Lenny")
                .put("2", "Lenny");
        assertThat(builder.isComplete()).isTrue();
    }

    @Test
    void isNotComplete() {
        PreferenceTable table = PreferenceTable.builder()
                .put("Lenny", "1", PreferenceType.YES)
                .put("Carl", "1", PreferenceType.YES)
                .put("Lenny", "2", PreferenceType.YES)
                .put("Carl", "2", PreferenceType.YES)
                .build();
        ScheduleBuilder builder = ScheduleBuilder.forPreferenceTable(table)
                .put("1", "Lenny");
        assertThat(builder.isComplete()).isFalse();
    }

    @Test
    void assignablePersonsEmptySchedule() {
        PreferenceTable table = PreferenceTable.builder()
                .put("Lenny", "1", PreferenceType.YES)
                .put("Carl", "1", PreferenceType.YES)
                .put("Lenny", "2", PreferenceType.YES)
                .put("Carl", "2", PreferenceType.YES)
                .build();
        ScheduleBuilder builder = ScheduleBuilder.forPreferenceTable(table);
        assertThat(builder.getAssignablePersons()).containsExactlyInAnyOrderEntriesOf(Map.of(
                "1", Set.of("Lenny", "Carl"),
                "2", Set.of("Lenny", "Carl")));
    }

    @Test
    void assignablePersonsUpdated() {
        PreferenceTable table = PreferenceTable.builder()
                .put("Lenny", "1", PreferenceType.YES)
                .put("Carl", "1", PreferenceType.YES)
                .put("Lenny", "2", PreferenceType.YES)
                .put("Carl", "2", PreferenceType.YES)
                .build();
        ScheduleBuilder builder = ScheduleBuilder.forPreferenceTable(table)
                        .put("1", "Lenny");
        assertThat(builder.getAssignablePersons()).containsExactlyInAnyOrderEntriesOf(Map.of(
                "2", Set.of("Carl")));
    }
}
