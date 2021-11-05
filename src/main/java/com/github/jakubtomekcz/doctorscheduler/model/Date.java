package com.github.jakubtomekcz.doctorscheduler.model;

import java.text.SimpleDateFormat;

public record Date(java.util.Date value) {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd MMM yyyy");

    @Override
    public String toString() {
        return dateFormat.format(value);
    }
}
