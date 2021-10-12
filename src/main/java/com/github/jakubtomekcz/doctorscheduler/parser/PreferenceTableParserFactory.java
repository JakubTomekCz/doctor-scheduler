package com.github.jakubtomekcz.doctorscheduler.parser;

import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
public class PreferenceTableParserFactory {

    public PreferenceTableParser getParserForFileExtension(String fileExtension) {
        switch (fileExtension) {
            default:
                throw new IllegalArgumentException(format("Unknown file extension: %s", fileExtension));
        }
    }
}
