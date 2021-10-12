package com.github.jakubtomekcz.doctorscheduler.parser;

import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
public class PreferenceTableParserFactory {

    public PreferenceTableParser getParserForContentType(String contentType) {
        switch (contentType) {
            default:
                throw new IllegalArgumentException(format("Unknown content type: %s", contentType));
        }
    }
}
