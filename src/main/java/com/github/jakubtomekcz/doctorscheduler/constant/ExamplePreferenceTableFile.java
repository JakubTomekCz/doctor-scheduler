package com.github.jakubtomekcz.doctorscheduler.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExamplePreferenceTableFile {

    XSLX_EN("preference-table-example-en.xlsx"),
    XSLX_CZ("preference-table-example-cz.xlsx");

    private final String filename;

}
