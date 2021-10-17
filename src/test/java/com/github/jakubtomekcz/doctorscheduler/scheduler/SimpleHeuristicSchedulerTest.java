package com.github.jakubtomekcz.doctorscheduler.scheduler;


import com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType;
import com.github.jakubtomekcz.doctorscheduler.model.PreferenceTable;
import com.github.jakubtomekcz.doctorscheduler.model.Schedule;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleHeuristicSchedulerTest {

    private SimpleHeuristicScheduler scheduler = new SimpleHeuristicScheduler();

    @Test
    void equalDistributionOfServiceDays() {
        List<String> musketeers = List.of("Atos", "Portos", "Aramis", "d'Artagnan");
        PreferenceTable.Builder builder = PreferenceTable.builder();
        for (int i = 1; i <= 16; i++) {
            String date = String.valueOf(i);
            musketeers.forEach(person -> builder.put(person, date, PreferenceType.YES));
        }
        PreferenceTable preferenceTable = builder.build();
        Schedule result = scheduler.createSchedule(preferenceTable);
        assertThat(result).satisfies(schedule ->
            musketeers.forEach(person -> assertThat(schedule.getServiceDaysCountForPerson(person)).isEqualTo(4)));
    }
}
