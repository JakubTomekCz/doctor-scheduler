package com.github.jakubtomekcz.doctorscheduler.model;

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
}
