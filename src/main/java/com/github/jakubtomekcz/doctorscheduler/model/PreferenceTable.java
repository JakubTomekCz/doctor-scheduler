package com.github.jakubtomekcz.doctorscheduler.model;


import com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType;
import com.google.common.collect.ImmutableMap;
import lombok.EqualsAndHashCode;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@EqualsAndHashCode
public class PreferenceTable {

    private PreferenceTable(ImmutableMap<String, ImmutableMap<String, PreferenceType>> data) {
        this.data = data;
    }

    private final ImmutableMap<String, ImmutableMap<String, PreferenceType>> data;

    public static Builder builder() {
        return new Builder();
    }

    public PreferenceType getPreference(String person, String date) {
        return data.get(date).get(person);
    }

    public List<String> getDates() {
        return data.keySet().asList();
    }

    public List<String> getPersons() {
        return data.entrySet().stream()
                .findFirst()
                .map(Map.Entry::getValue)
                .map(Map::entrySet)
                .orElse(Set.of())
                .stream()
                .map(Map.Entry::getKey)
                .collect(toList());
    }

    public static class Builder {

        private final Map<String, ImmutableMap.Builder<String, PreferenceType>> data = new LinkedHashMap<>();

        public Builder put(String person, String date, PreferenceType preference) {
            if (!data.containsKey(date)) {
                data.put(date, ImmutableMap.builder());
            }
            data.get(date).put(person, preference);
            return this;
        }

        public PreferenceTable build() {
            ImmutableMap.Builder<String, ImmutableMap<String, PreferenceType>> builder = ImmutableMap.builder();
            for (Map.Entry<String, ImmutableMap.Builder<String, PreferenceType>> entry : data.entrySet()) {
                builder.put(entry.getKey(), entry.getValue().build());
            }
            return new PreferenceTable(builder.build());
        }
    }
}
