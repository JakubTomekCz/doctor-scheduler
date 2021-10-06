package com.github.jakubtomekcz.doctorscheduler.constant;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.github.jakubtomekcz.doctorscheduler.constant.ExamplePreferenceTableFile.XSLX_CZ;
import static com.github.jakubtomekcz.doctorscheduler.constant.ExamplePreferenceTableFile.XSLX_EN;
import static org.assertj.core.api.Assertions.assertThat;

class ExamplePreferenceTableFileTest {

    @ParameterizedTest
    @MethodSource("getContentTypeSource")
    void getContentType(ExamplePreferenceTableFile file, String expectedContentType) {
        assertThat(file.getContentType()).isEqualTo(expectedContentType);
    }

    private static Stream<Arguments> getContentTypeSource() {
        return Stream.of(
                Arguments.of(XSLX_EN, "application/xlsx"),
                Arguments.of(XSLX_CZ, "application/xlsx")                );
    }

}