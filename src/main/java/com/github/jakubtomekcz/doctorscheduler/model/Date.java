package com.github.jakubtomekcz.doctorscheduler.model;

import com.google.common.annotations.VisibleForTesting;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public record Date(java.util.Date value) {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd MMM yyyy");

    @VisibleForTesting
    public static Date date(String date) {
        LocalDateTime localDateTime = LocalDateTime.parse(date + "T00:00:00");
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return new Date(java.util.Date.from(instant));
    }

    @VisibleForTesting
    public static Date dDayPlusNDays(int days) {
        LocalDateTime dDay = LocalDateTime.parse("2021-11-01T00:00:00");
        LocalDateTime dDayPlusNDays = dDay.plusDays(days);
        Instant instant = dDayPlusNDays.atZone(ZoneId.systemDefault()).toInstant();
        return new Date(java.util.Date.from(instant));
    }

    @Override
    public String toString() {
        return dateFormat.format(value);
    }
}
