package com.github.jakubtomekcz.doctorscheduler.schedule;

import com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class SatisfactionCriteriaTest {

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("isBetterMethodSource")
    void isBetter(String testName, ScheduleBuilder betterSchedule, ScheduleBuilder worseSchedule) {
        SatisfactionCriteria betterCriteria = SatisfactionCriteria.of(betterSchedule);
        SatisfactionCriteria worseCriteria = SatisfactionCriteria.of(worseSchedule);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(betterCriteria.isBetterThan(worseCriteria))
                    .as("better is better")
                    .isTrue();
            softly.assertThat(worseCriteria.isBetterThan(betterCriteria))
                    .as("worse is not better")
                    .isFalse();
        });
    }

    private static Stream<Arguments> isBetterMethodSource() {
        PreferenceTable table = preferenceTable();
        return Stream.of(
                Arguments.of("One preferred day granted is better than none",
                        ScheduleBuilder.forPreferenceTable(table)
                                .put("1", "Lenny")
                                .put("2", "Homer")
                                .put("3", "Carl")
                                .put("4", "Barney")
                                .put("5", "Lenny")
                                .put("6", "Homer")
                                .put("7", "Carl"),
                        ScheduleBuilder.forPreferenceTable(table)
                                .put("1", "Barney")
                                .put("2", "Homer")
                                .put("3", "Carl")
                                .put("4", "Barney")
                                .put("5", "Lenny")
                                .put("6", "Homer")
                                .put("7", "Carl")),

                Arguments.of("Two preferred days granted are better than one",
                        ScheduleBuilder.forPreferenceTable(table)
                                .put("1", "Lenny")
                                .put("2", "Homer")
                                .put("3", "Carl")
                                .put("4", "Lenny")
                                .put("5", "Barney")
                                .put("6", "Homer")
                                .put("7", "Carl"),
                        ScheduleBuilder.forPreferenceTable(table)
                                .put("1", "Lenny")
                                .put("2", "Homer")
                                .put("3", "Carl")
                                .put("4", "Barney")
                                .put("5", "Lenny")
                                .put("6", "Homer")
                                .put("7", "Carl")),

                Arguments.of("Two preferred days granted are better when split between two people",
                        ScheduleBuilder.forPreferenceTable(table)
                                .put("1", "Carl")
                                .put("2", "Barney")
                                .put("3", "Homer")
                                .put("4", "Lenny")
                                .put("5", "Barney")
                                .put("6", "Homer")
                                .put("7", "Carl"),
                        ScheduleBuilder.forPreferenceTable(table)
                                .put("1", "Lenny")
                                .put("2", "Homer")
                                .put("3", "Carl")
                                .put("4", "Lenny")
                                .put("5", "Barney")
                                .put("6", "Homer")
                                .put("7", "Carl")),

                Arguments.of("It's better when nobody is overworking",
                        ScheduleBuilder.forPreferenceTable(table)
                                .put("1", "Barney")
                                .put("2", "Lenny")
                                .put("3", "Carl")
                                .put("4", "Barney")
                                .put("5", "Lenny")
                                .put("6", "Carl")
                                .put("7", "Homer"),
                        ScheduleBuilder.forPreferenceTable(table)
                                .put("1", "Barney")
                                .put("2", "Lenny")
                                .put("3", "Carl")
                                .put("4", "Barney")
                                .put("5", "Homer")
                                .put("6", "Carl")
                                .put("7", "Barney")),

                Arguments.of("It's better when nobody is slacking off",
                        ScheduleBuilder.forPreferenceTable(table)
                                .put("1", "Barney")
                                .put("2", "Lenny")
                                .put("3", "Carl")
                                .put("4", "Barney")
                                .put("5", "Homer")
                                .put("6", "Lenny")
                                .put("7", "Barney"),
                        ScheduleBuilder.forPreferenceTable(table)
                                .put("1", "Barney")
                                .put("2", "Lenny")
                                .put("3", "Carl")
                                .put("4", "Barney")
                                .put("5", "Lenny")
                                .put("6", "Carl")
                                .put("7", "Barney")));
    }

    private static PreferenceTable preferenceTable() {
        return PreferenceTable.builder()
                .put("Homer", "1", PreferenceType.YES)
                .put("Lenny", "1", PreferenceType.PREFER)
                .put("Carl", "1", PreferenceType.PREFER)
                .put("Barney", "1", PreferenceType.YES)

                .put("Homer", "2", PreferenceType.YES)
                .put("Lenny", "2", PreferenceType.YES)
                .put("Carl", "2", PreferenceType.YES)
                .put("Barney", "2", PreferenceType.YES)

                .put("Homer", "3", PreferenceType.YES)
                .put("Lenny", "3", PreferenceType.YES)
                .put("Carl", "3", PreferenceType.YES)
                .put("Barney", "3", PreferenceType.YES)

                .put("Homer", "4", PreferenceType.YES)
                .put("Lenny", "4", PreferenceType.PREFER)
                .put("Carl", "4", PreferenceType.YES)
                .put("Barney", "4", PreferenceType.YES)

                .put("Homer", "5", PreferenceType.YES)
                .put("Lenny", "5", PreferenceType.YES)
                .put("Carl", "5", PreferenceType.YES)
                .put("Barney", "5", PreferenceType.YES)

                .put("Homer", "6", PreferenceType.YES)
                .put("Lenny", "6", PreferenceType.YES)
                .put("Carl", "6", PreferenceType.YES)
                .put("Barney", "6", PreferenceType.YES)

                .put("Homer", "7", PreferenceType.YES)
                .put("Lenny", "7", PreferenceType.YES)
                .put("Carl", "7", PreferenceType.YES)
                .put("Barney", "7", PreferenceType.YES)
                .build();
    }
}
