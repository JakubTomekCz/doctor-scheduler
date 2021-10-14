package com.github.jakubtomekcz.doctorscheduler.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class UiMessageException extends RuntimeException {

    private final MessageCode messageCode;
    private final Object[] messageParams;

    public UiMessageException(MessageCode messageCode, Object... messageParams) {
        this.messageCode = messageCode;
        this.messageParams = messageParams;
    }

    @Getter
    @RequiredArgsConstructor
    public enum MessageCode {

        UPLOAD_FILE_TOO_BIG("error.upload.file.too.big"),
        UPLOAD_FILE_UNKNOWN_TYPE("error.upload.file.unknown.type"),
        ERROR_READING_XLSX_FILE("error.reading.xlsx.file"),
        XLSX_FILE_DATE_EXPECTED("error.reading.xlsx.file.date.expected"),
        XLSX_FILE_PERSON_NAME_TOO_LONG("error.reading.xlsx.file.person.name.too.long"),
        XLSX_FILE_PREFERENCE_EXPECTED("error.reading.xlsx.file.preference.expected");

        private final String messageCode;
    }
}
