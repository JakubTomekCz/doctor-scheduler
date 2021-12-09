package com.github.jakubtomekcz.doctorscheduler.constant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.NO;
import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.PREFER;
import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.YES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PreferenceTypeTest {

    @ParameterizedTest
    @MethodSource("fromStringSource")
    void fromString(String input, PreferenceType expectedResult) {
        assertThat(PreferenceType.fromString(input)).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> fromStringSource() {
        return Stream.of(
                Arguments.of("YES", YES),
                Arguments.of("ANO", YES),
                Arguments.of("NO", NO),
                Arguments.of("NE", NO),
                Arguments.of("PREFER", PREFER),
                Arguments.of("PREFERUJI", PREFER),
                Arguments.of("yes", YES),
                Arguments.of("ano", YES),
                Arguments.of("no", NO),
                Arguments.of("ne", NO),
                Arguments.of("prefer", PREFER),
                Arguments.of("preferuji", PREFER),
                Arguments.of("yEs", YES),
                Arguments.of("chci", PREFER),
                Arguments.of("CHCI", PREFER)
                );
    }

    @Test
    void nullValue() {
        assertThatThrownBy(() -> PreferenceType.fromString(null))
                .isInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = "nonsense")
    @EmptySource
    void illegalValue(String input) {
        assertThatThrownBy(() -> PreferenceType.fromString(input))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
