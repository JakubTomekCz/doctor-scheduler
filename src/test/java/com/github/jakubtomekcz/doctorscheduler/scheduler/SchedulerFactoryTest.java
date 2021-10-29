package com.github.jakubtomekcz.doctorscheduler.scheduler;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SchedulerFactoryTest {

    private final SchedulerFactory factory = new SchedulerFactory(10_000);

    @Test
    void createScheduler() {
        Scheduler scheduler = factory.createScheduler();
        assertThat(scheduler).isInstanceOf(HeuristicScheduler.class);
    }
}
