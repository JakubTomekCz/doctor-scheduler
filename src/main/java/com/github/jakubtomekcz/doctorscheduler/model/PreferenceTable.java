package com.github.jakubtomekcz.doctorscheduler.model;


import com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@EqualsAndHashCode
public class PreferenceTable {

    private final Map<String, Map<String, PreferenceType>> data = new HashMap<>();

}
