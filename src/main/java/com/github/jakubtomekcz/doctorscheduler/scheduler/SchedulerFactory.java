package com.github.jakubtomekcz.doctorscheduler.scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SchedulerFactory {

    private final int maxIterations;

    public SchedulerFactory(@Value("${algorithm.max.iterations}") int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public Scheduler createScheduler() {
        return new HeuristicScheduler(maxIterations);
    }
}
