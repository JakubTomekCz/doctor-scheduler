package com.github.jakubtomekcz.doctorscheduler.parser;

import org.springframework.stereotype.Service;

import java.util.Locale;

import static java.lang.String.format;

@Service
public class PreferenceTableParserFactory {

    public PreferenceTableParser getParserForFileExtension(String fileExtension) {
        switch (fileExtension.toUpperCase(Locale.ENGLISH)) {
            case "XLSX":
                return new XlsxParser();
            default:
                throw new IllegalArgumentException(format("Unknown file extension: %s", fileExtension));
        }
    }
}
