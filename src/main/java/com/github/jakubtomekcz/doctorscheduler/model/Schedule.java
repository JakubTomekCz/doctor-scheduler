package com.github.jakubtomekcz.doctorscheduler.model;

import com.google.common.collect.ImmutableMap;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

@EqualsAndHashCode
public class Schedule {

    /**
     * date -> person
     */
    private final ImmutableMap<String, String> data;

    public static Builder builderForPreferenceTable(PreferenceTable preferenceTable) {
        return new Builder(preferenceTable);
    }

    private Schedule(ImmutableMap<String, String> data) {
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

    public static class Builder {

        private final PreferenceTable preferenceTable;

        /**
         * date -> person
         */
        private final Map<String, String> data = new HashMap<>();

        private Builder(PreferenceTable preferenceTable) {
            this.preferenceTable = preferenceTable;
        }

        public Schedule build() {
            ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
            preferenceTable.getDates().forEach(date -> {
                if (data.get(date) == null) {
                    throw new NullPointerException(format("Cannot build Schedule. Missing person for date %s", date));
                }
                builder.put(date, data.get(date));
            });
            return new Schedule(builder.build());
        }

        public String get(String date) {
            return data.get(date);
        }

        public Builder put(String date, String person) {
            data.put(date, person);
            return this;
        }
    }
}
