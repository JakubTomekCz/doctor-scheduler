package com.github.jakubtomekcz.doctorscheduler.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExamplePreferenceTableFile {

    XSLX_EN("preference-table-example-en.xlsx"),
    XSLX_CZ("preference-table-example-cz.xlsx"),
    XSLX_2TAB_CZ("preference-table-example-two-tables-cz.xlsx");

    private final String filename;

    public String getContentType() {
        String suffix = filename.substring(filename.lastIndexOf('.') + 1);
        return "application/" + suffix;
    }

}
