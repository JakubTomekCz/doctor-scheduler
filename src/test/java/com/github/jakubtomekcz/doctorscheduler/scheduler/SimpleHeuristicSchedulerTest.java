package com.github.jakubtomekcz.doctorscheduler.scheduler;


import com.github.jakubtomekcz.doctorscheduler.model.PreferenceTable;
import com.github.jakubtomekcz.doctorscheduler.model.Schedule;
import com.github.jakubtomekcz.doctorscheduler.model.ScheduleBuilder;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.NO;
import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.PREFER;
import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.YES;
import static org.assertj.core.api.Assertions.assertThat;

class SimpleHeuristicSchedulerTest {

    private final SimpleHeuristicScheduler scheduler = new SimpleHeuristicScheduler();

    @Test
    void scheduleIsComplete() {
        List<String> musketeers = List.of("Atos", "Portos", "Aramis", "d'Artagnan");
        PreferenceTable.Builder builder = PreferenceTable.builder();
        for (int i = 1; i <= 32; i++) {
            String date = String.valueOf(i);
            musketeers.forEach(person -> builder.put(person, date, YES));
        }
        PreferenceTable preferenceTable = builder.build();
        Schedule result = scheduler.createSchedule(preferenceTable);
        assertThat(result).satisfies(schedule -> {
            assertThat(schedule.size()).isEqualTo(32);
            assertThat(schedule.getDates())
                    .isEqualTo(IntStream.rangeClosed(1, 32)
                            .boxed()
                            .map(String::valueOf)
                            .toList());
            assertThat(new HashSet<>(schedule.getPersonsOnlySchedule()))
                    .isSubsetOf("Atos", "Portos", "Aramis", "d'Artagnan");
        });
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

    @Test
    void refusalIsRespected() {
        List<String> musketeers = List.of("Atos", "Portos", "Aramis", "d'Artagnan");
        PreferenceTable.Builder builder = PreferenceTable.builder();
        musketeers.forEach(person -> builder.put(person, "1", person.equals("d'Artagnan") ? YES : NO));
        musketeers.forEach(person -> builder.put(person, "2", person.equals("Atos") ? YES : NO));
        musketeers.forEach(person -> builder.put(person, "3", person.equals("Portos") ? YES : NO));
        musketeers.forEach(person -> builder.put(person, "4", person.equals("Aramis") ? YES : NO));
        PreferenceTable preferenceTable = builder.build();
        Schedule actualResult = scheduler.createSchedule(preferenceTable);
        Schedule expectedResult = ScheduleBuilder.forPreferenceTable(preferenceTable)
                .put("1", "d'Artagnan")
                .put("2", "Atos")
                .put("3", "Portos")
                .put("4", "Aramis")
                .build();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void preferredDayIsRespected() {
        List<String> musketeers = List.of("Atos", "Portos", "Aramis", "d'Artagnan");
        PreferenceTable.Builder builder = PreferenceTable.builder();
        musketeers.forEach(person -> builder.put(person, "1", person.equals("d'Artagnan") ? PREFER : YES));
        musketeers.forEach(person -> builder.put(person, "2", person.equals("Atos") ? PREFER : YES));
        musketeers.forEach(person -> builder.put(person, "3", person.equals("Portos") ? PREFER : YES));
        musketeers.forEach(person -> builder.put(person, "4", person.equals("Aramis") ? PREFER : YES));
        PreferenceTable preferenceTable = builder.build();
        Schedule actualResult = scheduler.createSchedule(preferenceTable);
        Schedule expectedResult = ScheduleBuilder.forPreferenceTable(preferenceTable)
                .put("1", "d'Artagnan")
                .put("2", "Atos")
                .put("3", "Portos")
                .put("4", "Aramis")
                .build();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void equalDistributionOfServiceDays() {
        List<String> musketeers = List.of("Atos", "Portos", "Aramis", "d'Artagnan");
        PreferenceTable.Builder builder = PreferenceTable.builder();
        for (int i = 1; i <= 32; i++) {
            String date = String.valueOf(i);
            musketeers.forEach(person -> builder.put(person, date, YES));
        }
        PreferenceTable preferenceTable = builder.build();
        Schedule result = scheduler.createSchedule(preferenceTable);
        assertThat(result).satisfies(schedule ->
                musketeers.forEach(person -> assertThat(schedule.getServiceDaysCountForPerson(person)).isEqualTo(8)));
    }

    @Test
    void equalDistributionOfPreferredDays() {
        List<String> musketeers = List.of("Atos", "Portos", "Aramis", "d'Artagnan");
        PreferenceTable.Builder builder = PreferenceTable.builder();
        for (int i = 1; i <= 32; i++) {
            String date = String.valueOf(i);
            musketeers.forEach(person -> builder.put(person, date, PREFER));
        }
        PreferenceTable preferenceTable = builder.build();
        Schedule result = scheduler.createSchedule(preferenceTable);
        assertThat(result).satisfies(schedule ->
                musketeers.forEach(person -> assertThat(schedule.getServiceDaysCountForPerson(person)).isEqualTo(8)));
    }
}
