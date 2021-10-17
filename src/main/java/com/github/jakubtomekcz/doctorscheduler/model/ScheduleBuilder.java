package com.github.jakubtomekcz.doctorscheduler.model;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

public class ScheduleBuilder {

    public static ScheduleBuilder forPreferenceTable(PreferenceTable preferenceTable) {
        return new ScheduleBuilder(preferenceTable);
    }

    private final PreferenceTable preferenceTable;

    /**
     * date -> person
     */
    private final Map<String, String> data = new HashMap<>();

    private ScheduleBuilder(PreferenceTable preferenceTable) {
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

    public ScheduleBuilder put(String date, String person) {
        data.put(date, person);
        return this;
    }
}
