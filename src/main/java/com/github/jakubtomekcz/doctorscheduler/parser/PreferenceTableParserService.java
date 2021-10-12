package com.github.jakubtomekcz.doctorscheduler.parser;

import com.github.jakubtomekcz.doctorscheduler.error.UiMessageException;
import com.github.jakubtomekcz.doctorscheduler.model.PreferenceTable;
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
        PreferenceTableParser parser;
        try {
            parser = parserFactory.getParserForContentType(inputFile.getContentType());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new UiMessageException(UPLOAD_FILE_UNKNOWN_TYPE);
        }

        return parser.parseMultipartFile(inputFile);
    }

    private void checkFileSize(MultipartFile inputFile) {
        DataSize uploadedFileSize = DataSize.ofBytes(inputFile.getSize());
        if (uploadedFileSize.toBytes() > maxFileSize.toBytes()) {
            throw new UiMessageException(UPLOAD_FILE_TOO_BIG, uploadedFileSize.toString(), maxFileSize.toString());
        }
    }
}
