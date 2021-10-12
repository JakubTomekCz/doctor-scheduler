package com.github.jakubtomekcz.doctorscheduler.parser;

import com.github.jakubtomekcz.doctorscheduler.model.PreferenceTable;
import org.springframework.web.multipart.MultipartFile;

public interface PreferenceTableParser {

    PreferenceTable parseMultipartFile(MultipartFile multipartFile);

}
