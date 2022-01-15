package com.github.jakubtomekcz.doctorscheduler.model;

import com.google.common.collect.ImmutableMap;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode
public class Schedule {

    /**
     * date -> person
     */
    private final ImmutableMap<Date, Person> data;

    Schedule(ImmutableMap<Date, Person> data) {
        this.data = data;
    }

    public Person get(Date date) {
        return data.get(date);
    }

    public List<Person> getPersonsOnlySchedule() {
        return data.values().stream()
                .toList();
    }

    public List<Date> getDates() {
        return data.keySet().asList();
    }

    public int size() {
        return data.size();
    }

    public int getShiftDaysCountForPerson(Person person) {
        long longCount = data.entrySet().stream()
                .filter(entry -> entry.getValue().equals(person))
                .count();
        return Math.toIntExact(longCount);
    }

    public int getWeekendShiftDaysCountForPerson(Person person) {
        long longCount = data.entrySet().stream()
                .filter(entry -> entry.getKey().isWeekendDay())
                .filter(entry -> entry.getValue().equals(person))
                .count();
        return Math.toIntExact(longCount);
    }

    public int getWeekShiftDaysCountForPerson(Person person) {
        long longCount = data.entrySet().stream()
                .filter(entry -> entry.getKey().isWeekDay())
                .filter(entry -> entry.getValue().equals(person))
                .count();
        return Math.toIntExact(longCount);
    }
}
