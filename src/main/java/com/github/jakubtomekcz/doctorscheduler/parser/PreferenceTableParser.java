package com.github.jakubtomekcz.doctorscheduler.parser;

import com.github.jakubtomekcz.doctorscheduler.model.PreferenceTable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PreferenceTableParser {

    List<PreferenceTable> parseMultipartFile(MultipartFile multipartFile);

}
