package com.github.jakubtomekcz.doctorscheduler.schedule;

import com.github.jakubtomekcz.doctorscheduler.model.Date;
import com.github.jakubtomekcz.doctorscheduler.model.Person;
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

    public int getServiceDaysCountForPerson(Person person) {
        long longCount = data.entrySet().stream()
                .filter(entry -> entry.getValue().equals(person))
                .count();
        return Math.toIntExact(longCount);
    }
}
