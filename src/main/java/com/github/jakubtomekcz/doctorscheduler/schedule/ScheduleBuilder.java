package com.github.jakubtomekcz.doctorscheduler.schedule;

import com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.NO;
import static java.lang.String.format;

@Getter
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

    public ScheduleBuilder put(String date, String person) {
        data.put(date, person);
        return this;
    }

    public SatisfactionCriteria criteria() {
        return SatisfactionCriteria.of(this);
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

    /**
     * Checks if the current schedule satisfies the elementary requirements:
     * <p>
     * 1. Refusal of service indicated by the {@link PreferenceType#NO} preference must be respected
     * 2. Each person must have at least two days rest between two service days
     *
     * @return {@code true} if the requirements above are satisfied
     */
    public boolean isValid() {
        return isRefusalOfServiceRespected() && isThereAlwaysTwoDaysRestAfterService();
    }

    private boolean isRefusalOfServiceRespected() {
        return data.entrySet().stream()
                .noneMatch(entry -> preferenceTable.getPreference(entry.getValue(), entry.getKey()) == NO);
    }

    private boolean isThereAlwaysTwoDaysRestAfterService() {
        for (int dateIndex = 0; dateIndex < preferenceTable.getDates().size() - 1; dateIndex++) {
            if (isSamePersonScheduledOnDayIndexes(dateIndex, dateIndex + 1)
                    || dateIndex < preferenceTable.getDates().size() - 2
                    && isSamePersonScheduledOnDayIndexes(dateIndex, dateIndex + 2)) {
                return false;
            }
        }
        return true;
    }

    private boolean isSamePersonScheduledOnDayIndexes(int dateIndex1, int dateIndex2) {
        List<String> dates = preferenceTable.getDates();
        return data.containsKey(dates.get(dateIndex1))
                && data.containsKey(dates.get(dateIndex2))
                && data.get(dates.get(dateIndex1)).equals(data.get(dates.get(dateIndex2)));
    }
}
