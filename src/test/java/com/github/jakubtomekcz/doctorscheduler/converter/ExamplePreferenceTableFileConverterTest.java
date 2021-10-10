package com.github.jakubtomekcz.doctorscheduler.converter;


import com.github.jakubtomekcz.doctorscheduler.constant.ExamplePreferenceTableFile;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.core.convert.converter.Converter;

import static org.assertj.core.api.Assertions.assertThat;

class ExamplePreferenceTableFileConverterTest {

    @ParameterizedTest
    @EnumSource(ExamplePreferenceTableFile.class)
    void convert(ExamplePreferenceTableFile file) {
        Converter<String, ExamplePreferenceTableFile> converter = new ExamplePreferenceTableFileConverter();
        ExamplePreferenceTableFile actualResult = converter.convert(file.getFilename());
        assertThat(actualResult).isEqualTo(file);
    }
}
