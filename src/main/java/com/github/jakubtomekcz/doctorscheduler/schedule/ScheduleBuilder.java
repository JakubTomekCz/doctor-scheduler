package com.github.jakubtomekcz.doctorscheduler.schedule;

import com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType;
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
        if (!isValid()) {
            throw new IllegalStateException("Cannot build schedule. Elementary schedule requirements are not met.");
        }
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

    /**
     * Checks if the current schedule satisfies the elementary requirements:
     * <p>
     * 1. Refusal of service indicated by the {@link PreferenceType#NO} preference must be respected
     * 2. Each person must have at least two days rest between two service days
     * @return {@true} if the requirements above are satisfied
     */
    public boolean isValid() {
        return isRefusalOfServiceRespected() && isThereAlwaysTwoDaysRestAfterService();
    }

    private boolean isRefusalOfServiceRespected() {
        return false;
    }

    private boolean isThereAlwaysTwoDaysRestAfterService() {
        return false;
    }
}
