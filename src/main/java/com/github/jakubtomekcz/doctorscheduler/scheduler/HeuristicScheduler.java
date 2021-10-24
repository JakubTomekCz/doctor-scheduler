package com.github.jakubtomekcz.doctorscheduler.scheduler;

import com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType;
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
        String date = selectDateToBeAssignedAPerson(builder);

        return Optional.empty();
    }

    /**
     * Heuristic method of selecting the next date to be assigned a person
     * Priorities:
     * 1. Date with only one possible person
     * 2. Date where at least one possible person has {@link PreferenceType#PREFER} (prefer fewer)
     * 3. Other dates, prefer fewer possible persons
     */
    private String selectDateToBeAssignedAPerson(ScheduleBuilder builder) {
        return null;
    }
}
