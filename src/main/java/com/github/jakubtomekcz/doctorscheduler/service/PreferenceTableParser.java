package com.github.jakubtomekcz.doctorscheduler.service;

import com.github.jakubtomekcz.doctorscheduler.error.UiMessageException;
import com.github.jakubtomekcz.doctorscheduler.model.PreferenceTable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import static com.github.jakubtomekcz.doctorscheduler.error.UiMessageException.MessageCode.UPLOAD_FILE_TOO_BIG;

@Service
public class PreferenceTableParser {

    private final DataSize maxFileSize;

    public PreferenceTableParser(@Value("${max.upload.file.size}") DataSize maxFileSizeString) {
        this.maxFileSize = maxFileSizeString;
    }

    public PreferenceTable buildPreferenceTable(MultipartFile inputFile) {
        checkFileSize(inputFile);

        return null;
    }

    private void checkFileSize(MultipartFile inputFile) {
        DataSize uploadedFileSize = DataSize.ofBytes(inputFile.getSize());
        if (uploadedFileSize.toBytes() > maxFileSize.toBytes()) {
            throw new UiMessageException(UPLOAD_FILE_TOO_BIG, uploadedFileSize.toString(), maxFileSize.toString());
        }
    }

}
