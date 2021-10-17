package com.github.jakubtomekcz.doctorscheduler.scheduler;


import com.github.jakubtomekcz.doctorscheduler.model.PreferenceTable;
import com.github.jakubtomekcz.doctorscheduler.model.Schedule;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.NO;
import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.YES;
import static org.assertj.core.api.Assertions.assertThat;

class SimpleHeuristicSchedulerTest {

    private final SimpleHeuristicScheduler scheduler = new SimpleHeuristicScheduler();

    @Test
    void equalDistributionOfServiceDays() {
        List<String> musketeers = List.of("Atos", "Portos", "Aramis", "d'Artagnan");
        PreferenceTable.Builder builder = PreferenceTable.builder();
        for (int i = 1; i <= 16; i++) {
            String date = String.valueOf(i);
            musketeers.forEach(person -> builder.put(person, date, YES));
        }
        PreferenceTable preferenceTable = builder.build();
        Schedule result = scheduler.createSchedule(preferenceTable);
        assertThat(result).satisfies(schedule ->
                musketeers.forEach(person -> assertThat(schedule.getServiceDaysCountForPerson(person)).isEqualTo(4)));
    }

    @Test
    void refusalIsRespected() {
        List<String> musketeers = List.of("Atos", "Portos", "Aramis", "d'Artagnan");
        PreferenceTable.Builder builder = PreferenceTable.builder();
        musketeers.forEach(person -> builder.put(person, "1", person.equals("Atos") ? YES : NO));
        musketeers.forEach(person -> builder.put(person, "2", person.equals("Portos") ? YES : NO));
        musketeers.forEach(person -> builder.put(person, "3", person.equals("Aramis") ? YES : NO));
        musketeers.forEach(person -> builder.put(person, "4", person.equals("d'Artagnan") ? YES : NO));
        PreferenceTable preferenceTable = builder.build();
        Schedule actualResult = scheduler.createSchedule(preferenceTable);
        Schedule expectedResult = Schedule.builderForDates("1", "2", "3", "4")
                .put("1", "Atos")
                .put("2", "Portos")
                .put("3", "Aramis")
                .put("4", "d'Artagnan")
                .build();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void twoDaysRestBetweenServiceDays() {
        List<String> musketeers = List.of("Atos", "Portos", "Aramis", "d'Artagnan");
        PreferenceTable.Builder builder = PreferenceTable.builder();
        for (int i = 1; i <= 16; i++) {
            String date = String.valueOf(i);
            musketeers.forEach(person -> builder.put(person, date, YES));
        }
        PreferenceTable preferenceTable = builder.build();
        Schedule result = scheduler.createSchedule(preferenceTable);
        assertTwoDaysRestBetweenServiceDays(result);
    }

    private void assertTwoDaysRestBetweenServiceDays(Schedule schedule) {
        List<String> personsSchedule = schedule.getPersonsOnlySchedule();
        for (int i = 0; i < personsSchedule.size() - 1; i++) {
            assertThat(personsSchedule.get(i)).isNotEqualTo(personsSchedule.get(i + 1));
            if (i < personsSchedule.size() - 2) {
                assertThat(personsSchedule.get(i)).isNotEqualTo(personsSchedule.get(i + 2));
                assertThat(personsSchedule.get(i + 1)).isNotEqualTo(personsSchedule.get(i + 2));
            }
        }
    }
}
