package com.github.jakubtomekcz.doctorscheduler.parser;

import com.github.jakubtomekcz.doctorscheduler.error.UiMessageException;
import com.github.jakubtomekcz.doctorscheduler.schedule.PreferenceTable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import static com.github.jakubtomekcz.doctorscheduler.error.UiMessageException.MessageCode.UPLOAD_FILE_TOO_BIG;
import static com.github.jakubtomekcz.doctorscheduler.error.UiMessageException.MessageCode.UPLOAD_FILE_UNKNOWN_TYPE;

@Service
public class PreferenceTableParserService {

    private final DataSize maxFileSize;

    private final PreferenceTableParserFactory parserFactory;

    public PreferenceTableParserService(@Value("${max.upload.file.size}") DataSize maxFileSizeString,
                                        PreferenceTableParserFactory parserFactory) {
        this.maxFileSize = maxFileSizeString;
        this.parserFactory = parserFactory;
    }

    public PreferenceTable parseMultipartFile(MultipartFile inputFile) {
        checkFileSize(inputFile);
        String filename = inputFile.getOriginalFilename();
        String suffix = filename == null ? "" : getSuffix(inputFile.getOriginalFilename());
        PreferenceTableParser parser;
        try {
            parser = parserFactory.getParserForFileExtension(suffix);
        } catch (IllegalArgumentException e) {
            throw new UiMessageException(UPLOAD_FILE_UNKNOWN_TYPE);
        }

        return parser.parseMultipartFile(inputFile);
    }

    private String getSuffix(String filename) {
        return filename.substring(filename.lastIndexOf('.') + 1);
    }

    private void checkFileSize(MultipartFile inputFile) {
        DataSize uploadedFileSize = DataSize.ofBytes(inputFile.getSize());
        if (uploadedFileSize.toBytes() > maxFileSize.toBytes()) {
            throw new UiMessageException(UPLOAD_FILE_TOO_BIG, uploadedFileSize.toString(), maxFileSize.toString());
        }
    }
}
