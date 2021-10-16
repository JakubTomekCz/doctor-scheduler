package com.github.jakubtomekcz.doctorscheduler.scheduler;

import com.github.jakubtomekcz.doctorscheduler.model.PreferenceTable;
import com.github.jakubtomekcz.doctorscheduler.model.Schedule;

public interface Scheduler {

    Schedule createSchedule(PreferenceTable preferenceTable);
}
