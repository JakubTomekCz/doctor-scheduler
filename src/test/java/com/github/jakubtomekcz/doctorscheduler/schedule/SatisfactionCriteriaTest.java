package com.github.jakubtomekcz.doctorscheduler.schedule;

import com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType;
import com.github.jakubtomekcz.doctorscheduler.model.PreferenceTable;
import com.github.jakubtomekcz.doctorscheduler.model.SatisfactionCriteria;
import com.github.jakubtomekcz.doctorscheduler.model.ScheduleBuilder;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.BARNEY;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.CARL;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.FRIDAY;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.HOMER;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.LENNY;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.MONDAY;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.SATURDAY;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.SUNDAY;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.THURSDAY;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.TUESDAY;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.WEDNESDAY;

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
                                .put(MONDAY, LENNY)
                                .put(TUESDAY, HOMER)
                                .put(WEDNESDAY, CARL)
                                .put(THURSDAY, BARNEY)
                                .put(FRIDAY, LENNY)
                                .put(SATURDAY, HOMER)
                                .put(SUNDAY, CARL),
                        ScheduleBuilder.forPreferenceTable(table)
                                .put(MONDAY, BARNEY)
                                .put(TUESDAY, HOMER)
                                .put(WEDNESDAY, CARL)
                                .put(THURSDAY, BARNEY)
                                .put(FRIDAY, LENNY)
                                .put(SATURDAY, HOMER)
                                .put(SUNDAY, CARL)),

                Arguments.of("Two preferred days granted are better than one",
                        ScheduleBuilder.forPreferenceTable(table)
                                .put(MONDAY, LENNY)
                                .put(TUESDAY, HOMER)
                                .put(WEDNESDAY, CARL)
                                .put(THURSDAY, LENNY)
                                .put(FRIDAY, BARNEY)
                                .put(SATURDAY, HOMER)
                                .put(SUNDAY, CARL),
                        ScheduleBuilder.forPreferenceTable(table)
                                .put(MONDAY, LENNY)
                                .put(TUESDAY, HOMER)
                                .put(WEDNESDAY, CARL)
                                .put(THURSDAY, BARNEY)
                                .put(FRIDAY, LENNY)
                                .put(SATURDAY, HOMER)
                                .put(SUNDAY, CARL)),

                Arguments.of("Two preferred days granted are better when split between two people",
                        ScheduleBuilder.forPreferenceTable(table)
                                .put(MONDAY, CARL)
                                .put(TUESDAY, BARNEY)
                                .put(WEDNESDAY, HOMER)
                                .put(THURSDAY, LENNY)
                                .put(FRIDAY, BARNEY)
                                .put(SATURDAY, HOMER)
                                .put(SUNDAY, CARL),
                        ScheduleBuilder.forPreferenceTable(table)
                                .put(MONDAY, LENNY)
                                .put(TUESDAY, HOMER)
                                .put(WEDNESDAY, CARL)
                                .put(THURSDAY, LENNY)
                                .put(FRIDAY, BARNEY)
                                .put(SATURDAY, HOMER)
                                .put(SUNDAY, CARL)),

                Arguments.of("It's better when nobody is overworking",
                        ScheduleBuilder.forPreferenceTable(table)
                                .put(MONDAY, BARNEY)
                                .put(TUESDAY, LENNY)
                                .put(WEDNESDAY, CARL)
                                .put(THURSDAY, BARNEY)
                                .put(FRIDAY, LENNY)
                                .put(SATURDAY, CARL)
                                .put(SUNDAY, HOMER),
                        ScheduleBuilder.forPreferenceTable(table)
                                .put(MONDAY, BARNEY)
                                .put(TUESDAY, LENNY)
                                .put(WEDNESDAY, CARL)
                                .put(THURSDAY, BARNEY)
                                .put(FRIDAY, HOMER)
                                .put(SATURDAY, CARL)
                                .put(SUNDAY, BARNEY)),

                Arguments.of("It's better when nobody is slacking off",
                        ScheduleBuilder.forPreferenceTable(table)
                                .put(MONDAY, BARNEY)
                                .put(TUESDAY, LENNY)
                                .put(WEDNESDAY, CARL)
                                .put(THURSDAY, BARNEY)
                                .put(FRIDAY, HOMER)
                                .put(SATURDAY, LENNY)
                                .put(SUNDAY, BARNEY),
                        ScheduleBuilder.forPreferenceTable(table)
                                .put(MONDAY, BARNEY)
                                .put(TUESDAY, LENNY)
                                .put(WEDNESDAY, CARL)
                                .put(THURSDAY, BARNEY)
                                .put(FRIDAY, LENNY)
                                .put(SATURDAY, CARL)
                                .put(SUNDAY, BARNEY)));
    }

    private static PreferenceTable preferenceTable() {
        return PreferenceTable.builder()
                .put(HOMER, MONDAY, PreferenceType.YES)
                .put(LENNY, MONDAY, PreferenceType.PREFER)
                .put(CARL, MONDAY, PreferenceType.PREFER)
                .put(BARNEY, MONDAY, PreferenceType.YES)

                .put(HOMER, TUESDAY, PreferenceType.YES)
                .put(LENNY, TUESDAY, PreferenceType.YES)
                .put(CARL, TUESDAY, PreferenceType.YES)
                .put(BARNEY, TUESDAY, PreferenceType.YES)

                .put(HOMER, WEDNESDAY, PreferenceType.YES)
                .put(LENNY, WEDNESDAY, PreferenceType.YES)
                .put(CARL, WEDNESDAY, PreferenceType.YES)
                .put(BARNEY, WEDNESDAY, PreferenceType.YES)

                .put(HOMER, THURSDAY, PreferenceType.YES)
                .put(LENNY, THURSDAY, PreferenceType.PREFER)
                .put(CARL, THURSDAY, PreferenceType.YES)
                .put(BARNEY, THURSDAY, PreferenceType.YES)

                .put(HOMER, FRIDAY, PreferenceType.YES)
                .put(LENNY, FRIDAY, PreferenceType.YES)
                .put(CARL, FRIDAY, PreferenceType.YES)
                .put(BARNEY, FRIDAY, PreferenceType.YES)

                .put(HOMER, SATURDAY, PreferenceType.YES)
                .put(LENNY, SATURDAY, PreferenceType.YES)
                .put(CARL, SATURDAY, PreferenceType.YES)
                .put(BARNEY, SATURDAY, PreferenceType.YES)

                .put(HOMER, SUNDAY, PreferenceType.YES)
                .put(LENNY, SUNDAY, PreferenceType.YES)
                .put(CARL, SUNDAY, PreferenceType.YES)
                .put(BARNEY, SUNDAY, PreferenceType.YES)
                .build();
    }
}
