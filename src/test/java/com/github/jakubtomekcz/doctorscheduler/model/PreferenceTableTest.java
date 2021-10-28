package com.github.jakubtomekcz.doctorscheduler.model;

import org.junit.jupiter.api.Test;

import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.NO;
import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.PREFER;
import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.YES;
import static org.assertj.core.api.Assertions.assertThat;

class PreferenceTableTest {

    private static final Person LENNY = new Person("Lenny");
    private static final Person CARL = new Person("Carl");
    private static final Person HOMER = new Person("Homer");
    private static final Date MONDAY = new Date("Monday");
    private static final Date TUESDAY = new Date("Tuesday");
    private static final Date WEDNESDAY = new Date("Wednesday");
    private static final Date THURSDAY = new Date("Thursday");

    @Test
    void preferenceTableContainsInsertedElements() {
        PreferenceTable preferenceTable = PreferenceTable.builder()
                .put(LENNY, MONDAY, YES)
                .put(LENNY, TUESDAY, NO)
                .put(LENNY, WEDNESDAY, PREFER)
                .put(CARL, MONDAY, NO)
                .put(CARL, TUESDAY, NO)
                .put(CARL, WEDNESDAY, NO)
                .build();

        assertThat(preferenceTable).satisfies(table -> {
            assertThat(table.getPreference(LENNY, MONDAY)).isEqualTo(YES);
            assertThat(table.getPreference(LENNY, TUESDAY)).isEqualTo(NO);
            assertThat(table.getPreference(LENNY, WEDNESDAY)).isEqualTo(PREFER);
            assertThat(table.getPreference(CARL, MONDAY)).isEqualTo(NO);
            assertThat(table.getPreference(CARL, TUESDAY)).isEqualTo(NO);
            assertThat(table.getPreference(CARL, WEDNESDAY)).isEqualTo(NO);
        });
    }

    @Test
    void datesAreReturnedInOrderOfInsertion() {
        PreferenceTable preferenceTable = PreferenceTable.builder()
                .put(HOMER, MONDAY, YES)
                .put(HOMER, TUESDAY, YES)
                .put(HOMER, WEDNESDAY, YES)
                .put(HOMER, THURSDAY, YES)
                .build();

        assertThat(preferenceTable.getDates()).containsExactly(MONDAY, TUESDAY, WEDNESDAY, THURSDAY);
    }
}
