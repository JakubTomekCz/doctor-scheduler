package com.github.jakubtomekcz.doctorscheduler.scheduler;

import com.github.jakubtomekcz.doctorscheduler.schedule.ScheduleBuilder;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;

/**
 * Sort the candidate persons for given date into an order in which they will get a chance to build a solution
 * Priorities:
 * 1. candidate that has a preference on this day
 * 1.a prefer the one with fewest preferred days granted so far
 * 1.b prefer the one with fewest preferred days requests among non-assigned dates
 * 2. candidate with the fewest service days assigned
 */
@RequiredArgsConstructor
public class HeuristicCandidatePersonComparator implements Comparator<String> {

    private final ScheduleBuilder builder;

    public static HeuristicCandidatePersonComparator with(ScheduleBuilder builder) {
        return new HeuristicCandidatePersonComparator(builder);
    }

    @Override
    public int compare(String o1, String o2) {
        return 0;
    }
}
