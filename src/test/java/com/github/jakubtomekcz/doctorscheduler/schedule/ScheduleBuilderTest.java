package com.github.jakubtomekcz.doctorscheduler.schedule;

import com.github.jakubtomekcz.doctorscheduler.model.Date;
import com.github.jakubtomekcz.doctorscheduler.model.Person;
import com.github.jakubtomekcz.doctorscheduler.model.PreferenceTable;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.NO;
import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.YES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScheduleBuilderTest {

    private static final Person LENNY = new Person("Lenny");
    private static final Person CARL = new Person("Carl");

    private static final Date MONDAY = new Date("Monday");
    private static final Date TUESDAY = new Date("Tuesday");

    @Test
    void build() {
        PreferenceTable table = PreferenceTable.builder()
                .put(LENNY, MONDAY, YES)
                .put(CARL, MONDAY, YES)
                .put(LENNY, TUESDAY, YES)
                .put(CARL, TUESDAY, YES)
                .build();
        ScheduleBuilder.forPreferenceTable(table)
                .put(MONDAY, LENNY)
                .put(TUESDAY, CARL)
                .build();
    }

    @Test
    void cannotBuildIncompleteSchedule() {
        PreferenceTable table = PreferenceTable.builder()
                .put(LENNY, MONDAY, YES)
                .put(CARL, MONDAY, YES)
                .put(LENNY, TUESDAY, YES)
                .put(CARL, TUESDAY, YES)
                .build();
        assertThatThrownBy(() -> ScheduleBuilder.forPreferenceTable(table).build())
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void isConsistent() {
        PreferenceTable table = PreferenceTable.builder()
                .put(LENNY, MONDAY, YES)
                .put(CARL, MONDAY, YES)
                .put(LENNY, TUESDAY, YES)
                .put(CARL, TUESDAY, YES)
                .build();
        ScheduleBuilder builder = ScheduleBuilder.forPreferenceTable(table)
                .put(MONDAY, LENNY)
                .put(TUESDAY, CARL);
        assertThat(builder.isConsistent()).isTrue();
    }

    @Test
    void isNotConsistentDisrespectsRefusal() {
        PreferenceTable table = PreferenceTable.builder()
                .put(LENNY, MONDAY, YES)
                .put(CARL, MONDAY, YES)
                .put(LENNY, TUESDAY, YES)
                .put(CARL, TUESDAY, NO)
                .build();
        ScheduleBuilder builder = ScheduleBuilder.forPreferenceTable(table)
                .put(MONDAY, LENNY)
                .put(TUESDAY, CARL);
        assertThat(builder.isConsistent()).isFalse();
    }

    @Test
    void isNotConsistentViolatesTwoDaysRest() {
        PreferenceTable table = PreferenceTable.builder()
                .put(LENNY, MONDAY, YES)
                .put(CARL, MONDAY, YES)
                .put(LENNY, TUESDAY, YES)
                .put(CARL, TUESDAY, YES)
                .build();
        ScheduleBuilder builder = ScheduleBuilder.forPreferenceTable(table)
                .put(MONDAY, LENNY)
                .put(TUESDAY, LENNY);
        assertThat(builder.isConsistent()).isFalse();
    }

    @Test
    void cannotBuildInconsistentSchedule() {
        PreferenceTable table = PreferenceTable.builder()
                .put(LENNY, MONDAY, YES)
                .put(CARL, MONDAY, YES)
                .put(LENNY, TUESDAY, YES)
                .put(CARL, TUESDAY, YES)
                .build();
        ScheduleBuilder builder = ScheduleBuilder.forPreferenceTable(table)
                .put(MONDAY, LENNY)
                .put(TUESDAY, LENNY);
        assertThatThrownBy(builder::build)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void isComplete() {
        PreferenceTable table = PreferenceTable.builder()
                .put(LENNY, MONDAY, YES)
                .put(CARL, MONDAY, YES)
                .put(LENNY, TUESDAY, YES)
                .put(CARL, TUESDAY, YES)
                .build();
        ScheduleBuilder builder = ScheduleBuilder.forPreferenceTable(table)
                .put(MONDAY, LENNY)
                .put(TUESDAY, LENNY);
        assertThat(builder.isComplete()).isTrue();
    }

    @Test
    void isNotComplete() {
        PreferenceTable table = PreferenceTable.builder()
                .put(LENNY, MONDAY, YES)
                .put(CARL, MONDAY, YES)
                .put(LENNY, TUESDAY, YES)
                .put(CARL, TUESDAY, YES)
                .build();
        ScheduleBuilder builder = ScheduleBuilder.forPreferenceTable(table)
                .put(MONDAY, LENNY);
        assertThat(builder.isComplete()).isFalse();
    }

    @Test
    void assignablePersonsEmptySchedule() {
        PreferenceTable table = PreferenceTable.builder()
                .put(LENNY, MONDAY, YES)
                .put(CARL, MONDAY, YES)
                .put(LENNY, TUESDAY, YES)
                .put(CARL, TUESDAY, YES)
                .build();
        ScheduleBuilder builder = ScheduleBuilder.forPreferenceTable(table);
        assertThat(builder.getAssignablePersons()).containsExactlyInAnyOrderEntriesOf(Map.of(
                MONDAY, Set.of(LENNY, CARL),
                TUESDAY, Set.of(LENNY, CARL)));
    }

    @Test
    void assignablePersonsUpdated() {
        PreferenceTable table = PreferenceTable.builder()
                .put(LENNY, MONDAY, YES)
                .put(CARL, MONDAY, YES)
                .put(LENNY, TUESDAY, YES)
                .put(CARL, TUESDAY, YES)
                .build();
        ScheduleBuilder builder = ScheduleBuilder.forPreferenceTable(table)
                .put(MONDAY, LENNY);
        assertThat(builder.getAssignablePersons()).containsExactlyInAnyOrderEntriesOf(Map.of(
                TUESDAY, Set.of(CARL)));
    }

    @Test
    void modifyingCopyDoesNotAffectOriginal() {
        PreferenceTable table = PreferenceTable.builder()
                .put(LENNY, MONDAY, YES)
                .put(CARL, MONDAY, YES)
                .put(LENNY, TUESDAY, YES)
                .put(CARL, TUESDAY, YES)
                .build();
        ScheduleBuilder builder = ScheduleBuilder.forPreferenceTable(table);
        ScheduleBuilder clone = builder.copy();

        builder.put(MONDAY, LENNY);

        assertThat(clone.getSchedule()).isEmpty();
        assertThat(clone.getAssignablePersons()).containsExactlyInAnyOrderEntriesOf(Map.of(
                MONDAY, Set.of(LENNY, CARL),
                TUESDAY, Set.of(LENNY, CARL)));
    }
}
