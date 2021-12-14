package com.github.jakubtomekcz.doctorscheduler.model;

import org.junit.jupiter.api.Test;

import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.CARL;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.HOMER;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.LENNY;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.MONDAY;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.THURSDAY;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.TUESDAY;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.WEDNESDAY;
import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.NO;
import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.PREFER;
import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.YES;
import static org.assertj.core.api.Assertions.assertThat;

class PreferenceTableTest {

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

    @Test
    void nameIsSetCorrectly() {
        PreferenceTable preferenceTable = PreferenceTable.builder()
                .name("Simpsons")
                .put(HOMER, MONDAY, YES)
                .build();

        assertThat(preferenceTable.getName()).isEqualTo("Simpsons");
    }
}
