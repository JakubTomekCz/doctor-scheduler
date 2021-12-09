package com.github.jakubtomekcz.doctorscheduler.constant;

import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Stream;

import static java.lang.String.format;

@Getter
public enum PreferenceType {

    YES("YES", "ANO"),
    NO("NO", "NE"),
    PREFER("PREFER", "PREFERUJI", "CHCI");

    private final String[] matchingTokens;

    PreferenceType(String... matchingTokens) {
        this.matchingTokens = matchingTokens;
    }

    public static PreferenceType fromString(String token) {
        return Stream.of(PreferenceType.values())
                .filter(preferenceType -> Arrays.asList(preferenceType.getMatchingTokens())
                        .contains(token.toUpperCase(Locale.ENGLISH)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        format("No PreferenceType matching the token %s", token)));
    }
}
