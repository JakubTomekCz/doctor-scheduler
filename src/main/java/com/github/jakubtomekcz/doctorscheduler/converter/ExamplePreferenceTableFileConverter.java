package com.github.jakubtomekcz.doctorscheduler.converter;

import com.github.jakubtomekcz.doctorscheduler.constant.ExamplePreferenceTableFile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class ExamplePreferenceTableFileConverter implements Converter <String, ExamplePreferenceTableFile> {

    @Override
    public ExamplePreferenceTableFile convert(String source) {
        return Stream.of(ExamplePreferenceTableFile.values())
                .filter(file -> file.getFilename().equals(source))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No such file name exists."));
    }
}
