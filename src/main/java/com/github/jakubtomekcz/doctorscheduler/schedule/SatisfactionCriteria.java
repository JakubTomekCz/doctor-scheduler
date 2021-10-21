package com.github.jakubtomekcz.doctorscheduler.schedule;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.LongStream;

import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.PREFER;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

/**
 * Defines which schedule is better and which is worse by comparing the following criteria:
 * <p>
 * Priority 1
 * Maximize total number of preferred days satisfied giving priority to less satisfied persons
 * (maximize number of (1+)x satisfied, then (2+)x satisfied etc.)
 * <p>
 * Priority 2
 * Minimize overwork measured by overwork index ~ sum((individual-overwork - average work)^2)
 * <p>
 * Priority 3
 * Minimize slacking off measured by slack off index ~ sum((individual-underwork - average work)^2)
 */
public class SatisfactionCriteria implements Comparable<SatisfactionCriteria> {

    /**
     * Number of people that were granted the at least number of preferred days equal to the list index+1
     * Example:
     * [7, 2, 1] means:
     * 7 people were granted 1 or more preferred day
     * 2 people were granted 2 or more preferred days
     * 1 person was granted 3 or more preferred days
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
        // person -> number of days granted
        Map<String, Long> daysGrantedPerPersonMap = scheduleBuilder.getData().entrySet().stream()
                .filter(e -> scheduleBuilder.getPreferenceTable().getPreference(e.getValue(), e.getKey()) == PREFER)
                .collect(groupingBy(Entry::getValue, counting()));

        // number of days granted -> number of people with this score
        Map<Long, Long> daysGrantedPeopleCountMap = daysGrantedPerPersonMap.values().stream()
                .collect(groupingBy(identity(), counting()));

        long maxDaysGranted = daysGrantedPerPersonMap.values().stream().max(Long::compare).orElse(0L);
        return LongStream.rangeClosed(1, maxDaysGranted).boxed()
                .map(lng -> daysGrantedPeopleCountMap.entrySet().stream()
                        .filter(entry -> entry.getKey() >= lng)
                        .map(Entry::getValue)
                        .reduce(Long::sum)
                        .orElse(0L))
                .map(Math::toIntExact)
                .toList();
    }

    private static int calculateOverworkIndex(ScheduleBuilder scheduleBuilder) {
        int numberOfDays = scheduleBuilder.getPreferenceTable().getDates().size();
        int numberOfPeople = scheduleBuilder.getPreferenceTable().getPersons().size();
        int avgLow = numberOfDays / numberOfPeople;
        int avgHigh = numberOfPeople * avgLow == numberOfDays ? avgLow : avgLow + 1;

        return scheduleBuilder.getData().entrySet().stream()
                .collect(groupingBy(Entry::getValue, counting()))
                .values().stream()
                .map(Math::toIntExact)
                .filter(i -> i > avgHigh)
                .map(i -> i - avgHigh)
                .map(i -> i * i)
                .reduce(Integer::sum)
                .orElse(0);
    }

    private static int calculateSlackOffIndex(ScheduleBuilder scheduleBuilder) {
        int numberOfDays = scheduleBuilder.getPreferenceTable().getDates().size();
        int numberOfPeople = scheduleBuilder.getPreferenceTable().getPersons().size();
        int avgLow = numberOfDays / numberOfPeople;

        return scheduleBuilder.getData().entrySet().stream()
                .collect(groupingBy(Entry::getValue, counting()))
                .values().stream()
                .map(Math::toIntExact)
                .filter(i -> i < avgLow)
                .map(i -> avgLow - i)
                .map(i -> i * i)
                .reduce(Integer::sum)
                .orElse(0);
    }

    @Override
    public int compareTo(SatisfactionCriteria other) {
        int preferredDaysComparison = comparePreferredDaysGranted(preferredDaysGranted, other.preferredDaysGranted);
        if (preferredDaysComparison != 0) {
            return preferredDaysComparison;
        }

        if (overworkIndex != other.overworkIndex) {
            return overworkIndex - other.overworkIndex;
        }

        return slackOffIndex - other.slackOffIndex;
    }

    private int comparePreferredDaysGranted(List<Integer> daysGranted1, List<Integer> daysGranted2) {
        for (int i = 0; i < Math.max(daysGranted1.size(), daysGranted2.size()); i++) {
            int score1 = i < daysGranted1.size() ? daysGranted1.get(i) : 0;
            int score2 = i < daysGranted2.size() ? daysGranted2.get(i) : 0;
            if (score1 != score2) {
                return score1 - score2;
            }
        }
        return 0;
    }

    public boolean isBetterThan(SatisfactionCriteria other) {
        return this.compareTo(other) > 0;
    }
}
