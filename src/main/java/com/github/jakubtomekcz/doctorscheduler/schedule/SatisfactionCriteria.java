package com.github.jakubtomekcz.doctorscheduler.schedule;

import java.util.List;

/**
 * Defines which schedule is better and which is worse by comparing the following criteria:
 * <p>
 * Priority 1
 * Maximize total number of preferred days satisfied giving priority to less satisfied persons
 * (maximize number of 1x satisfied, then 2x satisfied etc.)
 * <p>
 * Priority 2
 * Minimize overwork measured by overwork index ~ sum((individual-overwork - average work)^2)
 * <p>
 * Priority 3
 * Minimize slacking off measured by slack off index ~ sum((individual-underwork - average work)^2)
 */
public class SatisfactionCriteria implements Comparable<SatisfactionCriteria> {

    /**
     * Number of people that were granted the number of preferred days equal to the list index+1
     * Example:
     * [7, 2, 1] means:
     * 7 people were granted 1 preferred day
     * 2 people were granted 2 preferred days
     * 1 person was granted 3 preferred days
     */
    private final List<Integer> preferredDaysGranted;

    private final int overworkIndex;

    private final int slackOffIndex;

    private SatisfactionCriteria(List<Integer> preferredDaysGranted, int overworkIndex, int slackOffIndex) {
        this.preferredDaysGranted = preferredDaysGranted;
        this.overworkIndex = overworkIndex;
        this.slackOffIndex = slackOffIndex;
    }

    public static SatisfactionCriteria of(ScheduleBuilder scheduleBuilder) {
        List<Integer> preferredDaysGranted = calculatePreferredDaysGranted(scheduleBuilder);
        int overworkIndex = calculateOverworkIndex(scheduleBuilder);
        int slackOffIndex = calculateSlackOffIndex(scheduleBuilder);
        return new SatisfactionCriteria(preferredDaysGranted, overworkIndex, slackOffIndex);
    }

    private static List<Integer> calculatePreferredDaysGranted(ScheduleBuilder scheduleBuilder) {
        return List.of();
    }

    private static int calculateOverworkIndex(ScheduleBuilder scheduleBuilder) {
        return 0;
    }

    private static int calculateSlackOffIndex(ScheduleBuilder scheduleBuilder) {
        return 0;
    }

    @Override
    public int compareTo(SatisfactionCriteria o) {
        return 0;
    }
}
