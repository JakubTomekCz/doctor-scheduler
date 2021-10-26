package com.github.jakubtomekcz.doctorscheduler.schedule;

import com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType;
import com.github.jakubtomekcz.doctorscheduler.model.Date;
import com.github.jakubtomekcz.doctorscheduler.model.Person;
import com.github.jakubtomekcz.doctorscheduler.model.PreferenceTable;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class SatisfactionCriteriaTest {

    private static final Person LENNY = new Person("Lenny");
    private static final Person CARL = new Person("Carl");
    private static final Person HOMER = new Person("Homer");
    private static final Person BARNEY = new Person("Barney");

    private static final Date MONDAY = new Date("Monday");
    private static final Date TUESDAY = new Date("Tuesday");
    private static final Date WEDNESDAY = new Date("Wednesday");
    private static final Date THURSDAY = new Date("Thursday");
    private static final Date FRIDAY = new Date("Friday");
    private static final Date SATURDAY = new Date("Saturday");
    private static final Date SUNDAY = new Date("Sunday");

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
