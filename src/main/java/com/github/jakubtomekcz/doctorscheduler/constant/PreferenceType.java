package com.github.jakubtomekcz.doctorscheduler.constant;

public enum PreferenceType {

    YES("YES", "ANO"),
    NO("NO", "NE"),
    PREFER("PREFER", "PREFERUJI");

    private final String[] matchingTokens;

    PreferenceType(String... matchingTokens) {
        this.matchingTokens = matchingTokens;
    }
}
