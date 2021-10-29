package com.github.jakubtomekcz.doctorscheduler.scheduler;

import com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType;
import com.github.jakubtomekcz.doctorscheduler.model.Date;
import com.github.jakubtomekcz.doctorscheduler.model.Person;
import com.github.jakubtomekcz.doctorscheduler.model.ScheduleBuilder;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.Map;

import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.PREFER;

/**
 * Sort the candidate persons for given date into an order in which they will get a chance to build a solution
 * Priorities:
 * 1. candidate that has a preference on this day
 * 1.a prefer the one with fewer preferred days granted so far
 * 1.b prefer the one with fewer preferred days requests among non-assigned dates
 * 2. candidate with the fewer service days assigned
 * <p>
 * Preferred people come first
 * i.e. {@code compare(person1, person2)} should return 1 when {@code person1} comes before {@code person2}
 */
@RequiredArgsConstructor
public class HeuristicCandidatePersonComparator implements Comparator<Person> {

    private final ScheduleBuilder builder;

    private final Date date;

    public static HeuristicCandidatePersonComparator forScheduleAndDate(ScheduleBuilder builder, Date date) {
        return new HeuristicCandidatePersonComparator(builder, date);
    }

    @Override
    public int compare(Person person1, Person person2) {
        PreferenceType person1Preference = builder.getPreferenceTable().getPreference(person1, date);
        PreferenceType person2Preference = builder.getPreferenceTable().getPreference(person2, date);

        if (person1Preference == PREFER && person2Preference == PREFER) {
            return compareTwoCandidatesWithPreference(person1, person2);
        } else if (person1Preference == PREFER) {
            return -1;
        } else if (person2Preference == PREFER) {
            return 1;
        } else {
            return serviceDaysAssignedCount(person1) - serviceDaysAssignedCount(person2);
        }
    }

    private int compareTwoCandidatesWithPreference(Person person1, Person person2) {
        int person1PreferredDaysGrantedCount = preferredDaysGrantedCount(person1);
        int person2PreferredDaysGrantedCount = preferredDaysGrantedCount(person2);
        if (person1PreferredDaysGrantedCount == person2PreferredDaysGrantedCount) {
            return preferredDaysAmongAssignableDaysCount(person1) - preferredDaysAmongAssignableDaysCount(person2);
        } else {
            return person1PreferredDaysGrantedCount - person2PreferredDaysGrantedCount;
        }
    }

    private int preferredDaysAmongAssignableDaysCount(Person person) {
        return Math.toIntExact(builder.getAssignablePersons().entrySet().stream()
                .filter(entry -> entry.getValue().contains(person))
                .map(Map.Entry::getKey)
                .filter(date -> builder.getPreferenceTable().getPreference(person, date) == PREFER)
                .count());
    }

    private int preferredDaysGrantedCount(Person person1) {
        return Math.toIntExact(builder.getSchedule().entrySet().stream()
                .filter(entry -> entry.getValue().equals(person1))
                .filter(entry -> builder.getPreferenceTable().getPreference(person1, entry.getKey()) == PREFER)
                .count());
    }

    private int serviceDaysAssignedCount(Person person1) {
        return Math.toIntExact(builder.getSchedule().values().stream()
                .filter(person1::equals)
                .count());
    }
}
