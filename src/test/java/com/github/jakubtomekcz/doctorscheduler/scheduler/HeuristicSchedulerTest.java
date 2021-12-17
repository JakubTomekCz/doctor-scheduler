package com.github.jakubtomekcz.doctorscheduler.scheduler;


import com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants;
import com.github.jakubtomekcz.doctorscheduler.model.Date;
import com.github.jakubtomekcz.doctorscheduler.model.Person;
import com.github.jakubtomekcz.doctorscheduler.model.PreferenceTable;
import com.github.jakubtomekcz.doctorscheduler.model.Schedule;
import com.github.jakubtomekcz.doctorscheduler.model.ScheduleBuilder;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.BARNEY;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.CARL;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.HOMER;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.LENNY;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.MONDAY;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.THURSDAY;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.TUESDAY;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.WEDNESDAY;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.dDayPlusNDays;
import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.NO;
import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.PREFER;
import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.YES;
import static org.assertj.core.api.Assertions.assertThat;

class HeuristicSchedulerTest {

    private final HeuristicScheduler scheduler = new HeuristicScheduler(10_000);

    @Test
    void scheduleIsComplete() {
        List<Person> people = List.of(LENNY, CARL, HOMER, BARNEY);
        PreferenceTable.Builder builder = PreferenceTable.builder();
        for (int i = 1; i <= 32; i++) {
            Date date = dDayPlusNDays(i);
            people.forEach(person -> builder.put(person, date, YES));
        }
        PreferenceTable preferenceTable = builder.build();
        Optional<Schedule> result = scheduler.createSchedule(preferenceTable);
        assertThat(result).isPresent();
        assertThat(result.get()).satisfies(schedule -> {
            assertThat(schedule.size()).isEqualTo(32);
            assertThat(schedule.getDates())
                    .isEqualTo(IntStream.rangeClosed(1, 32)
                            .boxed()
                            .map(PersonAndDateTestConstants::dDayPlusNDays)
                            .toList());
            assertThat(new HashSet<>(schedule.getPersonsOnlySchedule()))
                    .isSubsetOf(LENNY, CARL, HOMER, BARNEY);
        });
    }

    @Test
    void twoDaysRestBetweenServiceDays() {
        List<Person> people = List.of(LENNY, CARL, HOMER, BARNEY);
        PreferenceTable.Builder builder = PreferenceTable.builder();
        for (int i = 1; i <= 16; i++) {
            Date date = dDayPlusNDays(i);
            people.forEach(person -> builder.put(person, date, YES));
        }
        PreferenceTable preferenceTable = builder.build();
        Optional<Schedule> result = scheduler.createSchedule(preferenceTable);
        assertThat(result).isPresent();
        assertTwoDaysRestBetweenServiceDays(result.get());
    }

    private void assertTwoDaysRestBetweenServiceDays(Schedule schedule) {
        List<Person> personsSchedule = schedule.getPersonsOnlySchedule();
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
        List<Person> people = List.of(LENNY, CARL, HOMER, BARNEY);
        PreferenceTable.Builder builder = PreferenceTable.builder();
        people.forEach(person -> builder.put(person, MONDAY, person.equals(BARNEY) ? YES : NO));
        people.forEach(person -> builder.put(person, TUESDAY, person.equals(LENNY) ? YES : NO));
        people.forEach(person -> builder.put(person, WEDNESDAY, person.equals(CARL) ? YES : NO));
        people.forEach(person -> builder.put(person, THURSDAY, person.equals(HOMER) ? YES : NO));
        PreferenceTable preferenceTable = builder.build();
        Optional<Schedule> actualResult = scheduler.createSchedule(preferenceTable);
        Schedule expectedResult = ScheduleBuilder.forPreferenceTable(preferenceTable)
                .put(MONDAY, BARNEY)
                .put(TUESDAY, LENNY)
                .put(WEDNESDAY, CARL)
                .put(THURSDAY, HOMER)
                .build();
        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(expectedResult);
    }

    @Test
    void preferredDayIsRespected() {
        List<Person> people = List.of(LENNY, CARL, HOMER, BARNEY);
        PreferenceTable.Builder builder = PreferenceTable.builder();
        people.forEach(person -> builder.put(person, MONDAY, person.equals(BARNEY) ? PREFER : YES));
        people.forEach(person -> builder.put(person, TUESDAY, person.equals(LENNY) ? PREFER : YES));
        people.forEach(person -> builder.put(person, WEDNESDAY, person.equals(CARL) ? PREFER : YES));
        people.forEach(person -> builder.put(person, THURSDAY, person.equals(HOMER) ? PREFER : YES));
        PreferenceTable preferenceTable = builder.build();
        Optional<Schedule> actualResult = scheduler.createSchedule(preferenceTable);
        Schedule expectedResult = ScheduleBuilder.forPreferenceTable(preferenceTable)
                .put(MONDAY, BARNEY)
                .put(TUESDAY, LENNY)
                .put(WEDNESDAY, CARL)
                .put(THURSDAY, HOMER)
                .build();
        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(expectedResult);
    }

    @Test
    void equalDistributionOfServiceDays() {
        List<Person> people = List.of(LENNY, CARL, HOMER, BARNEY);
        PreferenceTable.Builder builder = PreferenceTable.builder();
        for (int i = 1; i <= 32; i++) {
            Date date = dDayPlusNDays(i);
            people.forEach(person -> builder.put(person, date, YES));
        }
        PreferenceTable preferenceTable = builder.build();
        Optional<Schedule> result = scheduler.createSchedule(preferenceTable);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(preferenceTable.getDates()).hasSize(32);
        softly.assertThat(result).isPresent();
        softly.assertThat(result.get().getServiceDaysCountForPerson(LENNY)).as("Lenny").isEqualTo(8);
        softly.assertThat(result.get().getServiceDaysCountForPerson(CARL)).as("Carl").isEqualTo(8);
        softly.assertThat(result.get().getServiceDaysCountForPerson(HOMER)).as("Homer").isEqualTo(8);
        softly.assertThat(result.get().getServiceDaysCountForPerson(BARNEY)).as("Barney").isEqualTo(8);
        softly.assertAll();
    }

    @Test
    void equalDistributionOfPreferredDays() {
        List<Person> people = List.of(LENNY, CARL, HOMER, BARNEY);
        PreferenceTable.Builder builder = PreferenceTable.builder();
        for (int i = 1; i <= 32; i++) {
            Date date = dDayPlusNDays(i);
            people.forEach(person -> builder.put(person, date, PREFER));
        }
        PreferenceTable preferenceTable = builder.build();
        Optional<Schedule> result = scheduler.createSchedule(preferenceTable);
        assertThat(result).isPresent();
        assertThat(result.get()).satisfies(schedule ->
                people.forEach(person -> assertThat(schedule.getServiceDaysCountForPerson(person)).isEqualTo(8)));
    }

    @ParameterizedTest
    @MethodSource("impossibleRequirementsMethodSource")
    void impossibleRequirements(PreferenceTable table) {
        Optional<Schedule> result = scheduler.createSchedule(table);
        assertThat(result).isEmpty();
    }

    private static Stream<Arguments> impossibleRequirementsMethodSource() {
        return Stream.of(
                Arguments.of(PreferenceTable.builder()
                        .put(LENNY, MONDAY, YES)
                        .put(CARL, MONDAY, YES)
                        .put(HOMER, MONDAY, YES)
                        .put(LENNY, TUESDAY, YES)
                        .put(CARL, TUESDAY, YES)
                        .put(HOMER, TUESDAY, YES)
                        .put(LENNY, WEDNESDAY, NO)
                        .put(CARL, WEDNESDAY, NO)
                        .put(HOMER, WEDNESDAY, NO)
                        .build()),
                Arguments.of(PreferenceTable.builder()
                        .put(LENNY, MONDAY, YES)
                        .put(CARL, MONDAY, YES)
                        .put(LENNY, TUESDAY, YES)
                        .put(CARL, TUESDAY, YES)
                        .put(LENNY, WEDNESDAY, YES)
                        .put(CARL, WEDNESDAY, YES)
                        .build()));
    }
}
