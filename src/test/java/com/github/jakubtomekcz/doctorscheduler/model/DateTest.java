package com.github.jakubtomekcz.doctorscheduler.model;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Locale;
import java.util.stream.Stream;

import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.date;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class DateTest {

    private static final Locale CZECH = new Locale("cs");

    @ParameterizedTest
    @MethodSource("formatMethodSource")
    void format(Date date, Locale locale, String expectedResult) {
        String actualResult = date.format(locale);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> formatMethodSource() {
        return Stream.of(
                Arguments.of(date("2021-12-01"), Locale.ENGLISH, "Wed 01 Dec 2021"),
                Arguments.of(date("2021-12-02"), Locale.ENGLISH, "Thu 02 Dec 2021"),
                Arguments.of(date("2021-12-03"), Locale.ENGLISH, "Fri 03 Dec 2021"),
                Arguments.of(date("2021-12-04"), Locale.ENGLISH, "Sat 04 Dec 2021"),
                Arguments.of(date("2021-12-05"), Locale.ENGLISH, "Sun 05 Dec 2021"),
                Arguments.of(date("2021-12-06"), Locale.ENGLISH, "Mon 06 Dec 2021"),
                Arguments.of(date("2021-12-07"), Locale.ENGLISH, "Tue 07 Dec 2021"),
                Arguments.of(date("2021-12-08"), Locale.ENGLISH, "Wed 08 Dec 2021"),

                Arguments.of(date("2021-12-01"), CZECH, "1. St"),
                Arguments.of(date("2021-12-02"), CZECH, "2. Čt"),
                Arguments.of(date("2021-12-03"), CZECH, "3. Pá"),
                Arguments.of(date("2021-12-04"), CZECH, "4. So"),
                Arguments.of(date("2021-12-05"), CZECH, "5. Ne"),
                Arguments.of(date("2021-12-06"), CZECH, "6. Po"),
                Arguments.of(date("2021-12-07"), CZECH, "7. Út"),
                Arguments.of(date("2021-12-08"), CZECH, "8. St"),

                Arguments.of(date("2021-12-01"), Locale.CHINESE, "Wed 01 Dec 2021"));
    }

    @ParameterizedTest
    @MethodSource("isWeekDayMethodSource")
    void isWeekDay(Date date, boolean expectedResult) {
        assertThat(date.isWeekDay()).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> isWeekDayMethodSource() {
        return Stream.of(
                Arguments.of(date("2021-12-01"), true),
                Arguments.of(date("2021-12-02"), true),
                Arguments.of(date("2021-12-03"), true),
                Arguments.of(date("2021-12-04"), false),
                Arguments.of(date("2021-12-05"), false),
                Arguments.of(date("2021-12-06"), true),
                Arguments.of(date("2021-12-07"), true),
                Arguments.of(date("2021-12-08"), true));
    }

    @ParameterizedTest
    @MethodSource("isWeekendDayMethodSource")
    void isWeekendDay(Date date, boolean expectedResult) {
        assertThat(date.isWeekendDay()).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> isWeekendDayMethodSource() {
        return Stream.of(
                Arguments.of(date("2021-12-01"), false),
                Arguments.of(date("2021-12-02"), false),
                Arguments.of(date("2021-12-03"), false),
                Arguments.of(date("2021-12-04"), true),
                Arguments.of(date("2021-12-05"), true),
                Arguments.of(date("2021-12-06"), false),
                Arguments.of(date("2021-12-07"), false),
                Arguments.of(date("2021-12-08"), false));
    }
}
