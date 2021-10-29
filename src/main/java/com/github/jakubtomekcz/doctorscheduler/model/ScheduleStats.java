package com.github.jakubtomekcz.doctorscheduler.model;

import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class ScheduleStats {

    private final int minDaysAssigned;
    private final ImmutableList<Person> minDaysAssignedPeople;
    private final int maxDaysAssigned;
    private final ImmutableList<Person> maxDaysAssignedPeople;

    private final int preferenceRequestsGranted;
    private final int preferenceRequestsTotal;
    private final ImmutableList<Integer> preferenceRequestsDistribution;

    public static ScheduleStats forPreferencesAndSchedule(PreferenceTable preferenceTable, Schedule schedule) {
        return null;
    }
}
