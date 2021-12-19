package com.github.jakubtomekcz.doctorscheduler.model;

public record PreferenceTableWithSchedule(PreferenceTable preferenceTable,
                                          Schedule schedule) {

    public boolean hasSchedule() {
        return schedule != null;
    }
}
