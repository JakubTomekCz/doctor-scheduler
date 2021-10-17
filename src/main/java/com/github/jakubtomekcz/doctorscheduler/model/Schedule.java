package com.github.jakubtomekcz.doctorscheduler.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@EqualsAndHashCode
@Getter
public class Schedule {

    private final Map<String, String> data = new LinkedHashMap<>();

    public String get(String date) {
        return data.get(date);
    }

    public void put(String date, String person) {
        data.put(date, person);
    }

    public int getServiceDaysCountForPerson(String person) {
        long longCount = data.entrySet().stream()
                .filter(entry -> entry.getValue().equals(person))
                .count();
        return Math.toIntExact(longCount);
    }
}
