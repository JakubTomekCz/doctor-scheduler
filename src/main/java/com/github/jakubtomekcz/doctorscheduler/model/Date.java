package com.github.jakubtomekcz.doctorscheduler.model;

public record Date(String value) {

    public static Date date(String date) {
        return new Date(date);
    }
}
