package com.github.jakubtomekcz.doctorscheduler.parser;

import com.github.jakubtomekcz.doctorscheduler.model.PreferenceTable;
import org.springframework.web.multipart.MultipartFile;

public class XlsxParser implements PreferenceTableParser {

    @Override
    public PreferenceTable parseMultipartFile(MultipartFile multipartFile) {
        return null;
    }
}
