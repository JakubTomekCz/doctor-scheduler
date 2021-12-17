package com.github.jakubtomekcz.doctorscheduler.scheduler;

import com.github.jakubtomekcz.doctorscheduler.model.PreferenceTable;
import com.github.jakubtomekcz.doctorscheduler.model.Schedule;
import com.github.jakubtomekcz.doctorscheduler.model.ScheduleBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@MockitoSettings
class SchedulerServiceTest {

    @Mock
    private SchedulerFactory schedulerFactory;

    @Mock
    private Scheduler scheduler;

    @InjectMocks
    private SchedulerService schedulerService;

    @Test
    void createSchedule() {
        PreferenceTable preferenceTable = PreferenceTable.builder().build();
        Optional<Schedule> expectedResult = Optional.of(ScheduleBuilder.forPreferenceTable(preferenceTable).build());
        doReturn(scheduler).when(schedulerFactory).createScheduler();
        doReturn(expectedResult).when(scheduler).createSchedule(preferenceTable);

        Optional<Schedule> actualResult = schedulerService.createSchedule(preferenceTable);

        assertThat(actualResult).isSameAs(expectedResult);
    }
}
