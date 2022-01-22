package com.github.jakubtomekcz.doctorscheduler.model;

import com.google.common.collect.ImmutableMap;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Getter
@EqualsAndHashCode
public class Schedule {

    private final PreferenceTable preferenceTable;

    private final ImmutableMap<Date, Person> data;

    private final ScheduleSummary summary;

    Schedule(PreferenceTable preferenceTable, ImmutableMap<Date, Person> data) {
        this.preferenceTable = preferenceTable;
        this.data = data;
        this.summary = ScheduleSummary.of(this);
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
