package com.github.jakubtomekcz.doctorscheduler.scheduler;


import com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType;
import com.github.jakubtomekcz.doctorscheduler.model.Person;
import com.github.jakubtomekcz.doctorscheduler.model.PreferenceTable;
import com.github.jakubtomekcz.doctorscheduler.model.ScheduleBuilder;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.BARNEY;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.CARL;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.FRIDAY;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.HOMER;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.LENNY;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.MONDAY;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.SATURDAY;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.SUNDAY;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.THURSDAY;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.TUESDAY;
import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.WEDNESDAY;
import static org.assertj.core.api.Assertions.assertThat;

class HeuristicCandidatePersonComparatorTest {

    @Test
    void preferCandidateWithPreferenceAndFewerPreferenceDaysGranted() {
        ScheduleBuilder scheduleBuilder = ScheduleBuilder.forPreferenceTable(preferenceTable());
        scheduleBuilder.put(THURSDAY, LENNY);
        var comparator = HeuristicCandidatePersonComparator.forScheduleAndDate(scheduleBuilder, MONDAY);

        List<Person> sortedCandidates = Stream.of(LENNY, CARL).sorted(comparator).toList();

        assertThat(sortedCandidates).containsExactly(CARL, LENNY);
        assertThat(comparator.compare(CARL, LENNY)).isNegative();
        assertThat(comparator.compare(LENNY, CARL)).isPositive();
    }

    @Test
    void preferCandidateWithPreferenceAndFewerPreferenceRequestsAmongNonAssignedDates() {
        ScheduleBuilder scheduleBuilder = ScheduleBuilder.forPreferenceTable(preferenceTable());
        var comparator = HeuristicCandidatePersonComparator.forScheduleAndDate(scheduleBuilder, MONDAY);

        List<Person> sortedCandidates = Stream.of(LENNY, CARL).sorted(comparator).toList();

        assertThat(sortedCandidates).containsExactly(CARL, LENNY);
        assertThat(comparator.compare(CARL, LENNY)).isNegative();
        assertThat(comparator.compare(LENNY, CARL)).isPositive();
    }

    @Test
    void preferCandidateWithPreference() {
        ScheduleBuilder scheduleBuilder = ScheduleBuilder.forPreferenceTable(preferenceTable());
        var comparator = HeuristicCandidatePersonComparator.forScheduleAndDate(scheduleBuilder, THURSDAY);

        List<Person> sortedCandidates = Stream.of(LENNY, HOMER).sorted(comparator).toList();

        assertThat(sortedCandidates).containsExactly(LENNY, HOMER);
        assertThat(comparator.compare(LENNY, HOMER)).isNegative();
        assertThat(comparator.compare(HOMER, LENNY)).isPositive();
    }

    @Test
    void preferCandidateWithFewerDaysAssigned() {
        ScheduleBuilder scheduleBuilder = ScheduleBuilder.forPreferenceTable(preferenceTable());
        scheduleBuilder.put(TUESDAY, LENNY);
        var comparator = HeuristicCandidatePersonComparator.forScheduleAndDate(scheduleBuilder, SUNDAY);

        List<Person> sortedCandidates = Stream.of(LENNY, CARL).sorted(comparator).toList();

        assertThat(sortedCandidates).containsExactly(CARL, LENNY);
        assertThat(comparator.compare(CARL, LENNY)).isNegative();
        assertThat(comparator.compare(LENNY, CARL)).isPositive();
    }

    @Test
    void equallyGoodCandidates() {
        ScheduleBuilder scheduleBuilder = ScheduleBuilder.forPreferenceTable(preferenceTable());
        var comparator = HeuristicCandidatePersonComparator.forScheduleAndDate(scheduleBuilder, TUESDAY);
        assertThat(comparator.compare(HOMER, LENNY)).isZero();
        assertThat(comparator.compare(LENNY, HOMER)).isZero();
    }

    private static PreferenceTable preferenceTable() {
        return PreferenceTable.builder()
                .put(HOMER, MONDAY, PreferenceType.YES)
                .put(LENNY, MONDAY, PreferenceType.PREFER)
                .put(CARL, MONDAY, PreferenceType.PREFER)
                .put(BARNEY, MONDAY, PreferenceType.YES)

                .put(HOMER, TUESDAY, PreferenceType.YES)
                .put(LENNY, TUESDAY, PreferenceType.YES)
                .put(CARL, TUESDAY, PreferenceType.YES)
                .put(BARNEY, TUESDAY, PreferenceType.YES)

                .put(HOMER, WEDNESDAY, PreferenceType.YES)
                .put(LENNY, WEDNESDAY, PreferenceType.YES)
                .put(CARL, WEDNESDAY, PreferenceType.YES)
                .put(BARNEY, WEDNESDAY, PreferenceType.YES)

                .put(HOMER, THURSDAY, PreferenceType.YES)
                .put(LENNY, THURSDAY, PreferenceType.PREFER)
                .put(CARL, THURSDAY, PreferenceType.YES)
                .put(BARNEY, THURSDAY, PreferenceType.YES)

                .put(HOMER, FRIDAY, PreferenceType.YES)
                .put(LENNY, FRIDAY, PreferenceType.YES)
                .put(CARL, FRIDAY, PreferenceType.YES)
                .put(BARNEY, FRIDAY, PreferenceType.YES)

                .put(HOMER, SATURDAY, PreferenceType.YES)
                .put(LENNY, SATURDAY, PreferenceType.YES)
                .put(CARL, SATURDAY, PreferenceType.YES)
                .put(BARNEY, SATURDAY, PreferenceType.YES)

                .put(HOMER, SUNDAY, PreferenceType.YES)
                .put(LENNY, SUNDAY, PreferenceType.YES)
                .put(CARL, SUNDAY, PreferenceType.YES)
                .put(BARNEY, SUNDAY, PreferenceType.YES)
                .build();
    }
}
