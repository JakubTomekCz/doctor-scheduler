package com.github.jakubtomekcz.doctorscheduler.model;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Locale;

@Slf4j
public record Date(java.util.Date value) {

    private static final String ENGLISH_FORMAT = "EEE dd MMM yyyy";
    private static final String ENGLISH_DAY_IN_WEEK_FORMAT = "EEE";
    private static final String CZECH_DAY_FORMAT = "d. ";

    public String format(Locale locale) {
        switch (locale.getLanguage()) {
            case "cs":
                return new SimpleDateFormat(CZECH_DAY_FORMAT).format(value) + czechDayOfTheWeek(value);
            case "en":
            default:
                return new SimpleDateFormat(ENGLISH_FORMAT).format(value);
        }
    }

    private String czechDayOfTheWeek(java.util.Date value) {
        String englishDayOfTheWeek = new SimpleDateFormat(ENGLISH_DAY_IN_WEEK_FORMAT).format(value);
        switch (englishDayOfTheWeek) {
            case "Mon": return "Po";
            case "Tue": return "Út";
            case "Wed": return "St";
            case "Thu": return "Čt";
            case "Fri": return "Pá";
            case "Sat": return "So";
            case "Sun": return "Ne";
            default:
                log.error("Cannot format date {}. Unknown day of the week [{}]", this, englishDayOfTheWeek);
                return englishDayOfTheWeek;
        }
    }
}
