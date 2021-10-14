package com.github.jakubtomekcz.doctorscheduler.parser;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PreferenceTableParserFactoryTest {

    private final PreferenceTableParserFactory factory = new PreferenceTableParserFactory();

    @ParameterizedTest
    @MethodSource("getParserMethodSource")
    <T> void getParser(String extension, Class<T> expectedResultClass) {
        PreferenceTableParser actualResult = factory.getParserForFileExtension(extension);
        assertThat(actualResult.getClass()).isEqualTo(expectedResultClass);
    }

    private static Stream<Arguments> getParserMethodSource() {
        return Stream.of(
                Arguments.of("xlsx", XlsxParser.class),
                Arguments.of("xLsX", XlsxParser.class),
                Arguments.of("XLSX", XlsxParser.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "csv", "doc", "pdf", ".xlsx"})
    void unknownExtension(String extension) {
        assertThatThrownBy(() -> factory.getParserForFileExtension(extension))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void nullPointer() {
        assertThatThrownBy(() -> factory.getParserForFileExtension(null))
                .isInstanceOf(NullPointerException.class);
    }
}