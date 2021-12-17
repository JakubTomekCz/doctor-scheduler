package com.github.jakubtomekcz.doctorscheduler.scheduler;

import com.github.jakubtomekcz.doctorscheduler.model.PreferenceTable;
import com.github.jakubtomekcz.doctorscheduler.model.Schedule;

import java.util.Optional;

public interface Scheduler {

    Optional<Schedule> createSchedule(PreferenceTable preferenceTable);
}
