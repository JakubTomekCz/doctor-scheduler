package com.github.jakubtomekcz.doctorscheduler.model;

import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode
public class ScheduleSuggestion {

    private final Map<String, String> data = new HashMap<>();

    public String get(String date) {
        return data.get(date);
    }

    public void put(String date, String person) {
        data.put(date, person);
    }
}
