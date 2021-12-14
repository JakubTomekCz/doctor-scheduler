package com.github.jakubtomekcz.doctorscheduler.model;


import com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType;
import com.google.common.collect.ImmutableMap;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@EqualsAndHashCode
public class PreferenceTable {

    @Getter
    private final String name;

    private final ImmutableMap<Date, ImmutableMap<Person, PreferenceType>> data;

    private PreferenceTable(String name, ImmutableMap<Date, ImmutableMap<Person, PreferenceType>> data) {
        this.name = name;
        this.data = data;
    }

    public static Builder builder() {
        return new Builder();
    }

    public PreferenceType getPreference(Person person, Date date) {
        return data.get(date).get(person);
    }

    public List<Date> getDates() {
        return data.keySet().asList();
    }

    public List<Person> getPersons() {
        return data.entrySet().stream()
                .findFirst()
                .map(Map.Entry::getValue)
                .map(Map::entrySet)
                .orElse(Set.of())
                .stream()
                .map(Map.Entry::getKey)
                .toList();
    }

    public static class Builder {

        private String name;
        private final Map<Date, ImmutableMap.Builder<Person, PreferenceType>> data = new LinkedHashMap<>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder put(Person person, Date date, PreferenceType preference) {
            if (!data.containsKey(date)) {
                data.put(date, ImmutableMap.builder());
            }
            data.get(date).put(person, preference);
            return this;
        }

        public PreferenceTable build() {
            ImmutableMap.Builder<Date, ImmutableMap<Person, PreferenceType>> builder = ImmutableMap.builder();
            for (Map.Entry<Date, ImmutableMap.Builder<Person, PreferenceType>> entry : data.entrySet()) {
                builder.put(entry.getKey(), entry.getValue().build());
            }
            return new PreferenceTable(name, builder.build());
        }
    }
}
