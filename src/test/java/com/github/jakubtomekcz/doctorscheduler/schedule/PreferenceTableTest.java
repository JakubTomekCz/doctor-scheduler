package com.github.jakubtomekcz.doctorscheduler.schedule;

import org.junit.jupiter.api.Test;

import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.NO;
import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.PREFER;
import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.YES;
import static org.assertj.core.api.Assertions.assertThat;

class PreferenceTableTest {

    @Test
    void preferenceTableContainsInsertedElements() {
        PreferenceTable preferenceTable = PreferenceTable.builder()
                .put("Lenny", "Monday", YES)
                .put("Lenny", "Tuesday", NO)
                .put("Lenny", "Wednesday", PREFER)
                .put("Carl", "Monday", NO)
                .put("Carl", "Tuesday", NO)
                .put("Carl", "Wednesday", NO)
                .build();

        assertThat(preferenceTable).satisfies(table -> {
            assertThat(table.getPreference("Lenny", "Monday")).isEqualTo(YES);
            assertThat(table.getPreference("Lenny", "Tuesday")).isEqualTo(NO);
            assertThat(table.getPreference("Lenny", "Wednesday")).isEqualTo(PREFER);
            assertThat(table.getPreference("Carl", "Monday")).isEqualTo(NO);
            assertThat(table.getPreference("Carl", "Tuesday")).isEqualTo(NO);
            assertThat(table.getPreference("Carl", "Wednesday")).isEqualTo(NO);
        });
    }

    @Test
    void datesAreReturnedInOrderOfInsertion() {
        PreferenceTable preferenceTable = PreferenceTable.builder()
                .put("Homer", "Monday", YES)
                .put("Homer", "Tuesday", YES)
                .put("Homer", "Wednesday", YES)
                .put("Homer", "Thursday", YES)
                .build();

        assertThat(preferenceTable.getDates()).containsExactly("Monday", "Tuesday", "Wednesday", "Thursday");
    }
}
