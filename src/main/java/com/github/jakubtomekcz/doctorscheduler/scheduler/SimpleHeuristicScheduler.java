package com.github.jakubtomekcz.doctorscheduler.scheduler;

import com.github.jakubtomekcz.doctorscheduler.schedule.PreferenceTable;
import com.github.jakubtomekcz.doctorscheduler.schedule.Schedule;
import com.github.jakubtomekcz.doctorscheduler.schedule.ScheduleBuilder;

/**
 * Creates a schedule based on given preferences.
 * Attempts to maximize satisfaction.
 */
public class SimpleHeuristicScheduler implements Scheduler {

    @Override
    public Schedule createSchedule(PreferenceTable preferenceTable) {
        ScheduleBuilder builder = ScheduleBuilder.forPreferenceTable(preferenceTable);

        return builder.build();
    }
}
