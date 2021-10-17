package com.github.jakubtomekcz.doctorscheduler.model;

import com.google.common.collect.ImmutableMap;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode
public class Schedule {

    /**
     * date -> person
     */
    private final ImmutableMap<String, String> data;

    Schedule(ImmutableMap<String, String> data) {
        this.data = data;
    }

    public String get(String date) {
        return data.get(date);
    }

    public List<String> getPersonsOnlySchedule() {
        return data.values().stream()
                .toList();
    }

    public List<String> getDates() {
        return data.keySet().asList();
    }

    public int size() {
        return data.size();
    }

    public int getServiceDaysCountForPerson(String person) {
        long longCount = data.entrySet().stream()
                .filter(entry -> entry.getValue().equals(person))
                .count();
        return Math.toIntExact(longCount);
    }
}
