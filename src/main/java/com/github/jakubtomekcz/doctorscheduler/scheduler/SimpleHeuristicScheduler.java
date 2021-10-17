package com.github.jakubtomekcz.doctorscheduler.scheduler;

import com.github.jakubtomekcz.doctorscheduler.model.PreferenceTable;
import com.github.jakubtomekcz.doctorscheduler.model.Schedule;

/**
 * Creates a schedule based on given preferences.
 * Attempts to maximize satisfaction.
 * <p>
 * Satisfaction criteria:
 * <p>
 * Priority 1 - must satisfy or fail
 * Preference = NO => must not select person
 * One person must not be selected for two consecutive days or second day after the first service (needs 2 days rest)
 * <p>
 * Priority 2
 * Maximize total number of preferred days satisfied giving priority to less satisfied persons
 * (maximize number of 1x satisfied, then 2x satisfied etc.)
 * <p>
 * Priority 3
 * Minimize overwork measured by overwork index ~ sum((individual-overwork - average work)^2)
 * <p>
 * Priority 4
 * Minimize slacking off measured by slack off index ~ sum((individual-underwork - average work)^2)
 */
public class SimpleHeuristicScheduler implements Scheduler {

    @Override
    public Schedule createSchedule(PreferenceTable preferenceTable) {
        Schedule.Builder builder = Schedule.builderForDates(preferenceTable.getDates());

        return builder.build();
    }
}
