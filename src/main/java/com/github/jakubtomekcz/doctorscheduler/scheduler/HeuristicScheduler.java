package com.github.jakubtomekcz.doctorscheduler.scheduler;

import com.github.jakubtomekcz.doctorscheduler.error.UiMessageException;
import com.github.jakubtomekcz.doctorscheduler.schedule.PreferenceTable;
import com.github.jakubtomekcz.doctorscheduler.schedule.Schedule;
import com.github.jakubtomekcz.doctorscheduler.schedule.ScheduleBuilder;

import java.util.Optional;

import static com.github.jakubtomekcz.doctorscheduler.error.UiMessageException.MessageCode.CANNOT_BUILD_SCHEDULE;

/**
 * Creates a schedule based on given preferences.
 * Attempts to maximize satisfaction.
 */
public class HeuristicScheduler implements Scheduler {

    @Override
    public Schedule createSchedule(PreferenceTable preferenceTable) {
        ScheduleBuilder builder = ScheduleBuilder.forPreferenceTable(preferenceTable);

        return findSolutionFor(builder)
                .map(ScheduleBuilder::build)
                .orElseThrow(() -> new UiMessageException(CANNOT_BUILD_SCHEDULE));
    }

    private Optional<ScheduleBuilder> findSolutionFor(ScheduleBuilder builder) {
        return Optional.empty();
    }
}
