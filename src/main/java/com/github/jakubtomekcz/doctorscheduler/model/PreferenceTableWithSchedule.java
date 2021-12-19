package com.github.jakubtomekcz.doctorscheduler.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PreferenceTableWithSchedule {

    private final PreferenceTable preferenceTable;
    private final Schedule schedule;
}
