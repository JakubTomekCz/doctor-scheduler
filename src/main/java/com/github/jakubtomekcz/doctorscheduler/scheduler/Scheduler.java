package com.github.jakubtomekcz.doctorscheduler.scheduler;

import com.github.jakubtomekcz.doctorscheduler.schedule.PreferenceTable;
import com.github.jakubtomekcz.doctorscheduler.schedule.Schedule;

public interface Scheduler {

    Schedule createSchedule(PreferenceTable preferenceTable);
}
