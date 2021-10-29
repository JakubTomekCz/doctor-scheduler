package com.github.jakubtomekcz.doctorscheduler.scheduler;

import com.github.jakubtomekcz.doctorscheduler.model.PreferenceTable;
import com.github.jakubtomekcz.doctorscheduler.model.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final SchedulerFactory factory;

    public Schedule createSchedule(PreferenceTable preferenceTable) {
        Scheduler scheduler = factory.createScheduler();
        return scheduler.createSchedule(preferenceTable);
    }
}
