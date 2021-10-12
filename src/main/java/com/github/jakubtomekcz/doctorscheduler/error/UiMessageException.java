package com.github.jakubtomekcz.doctorscheduler.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class UiMessageException extends RuntimeException {

    private final MessageCode messageCode;
    private final String[] messageParams;

    public UiMessageException(MessageCode messageCode, String... messageParams) {
        this.messageCode = messageCode;
        this.messageParams = messageParams;
    }

    @Getter
    @RequiredArgsConstructor
    public enum MessageCode {

        UPLOAD_FILE_TOO_BIG("error.upload.file.too.big"),
        UPLOAD_FILE_UNKNOWN_TYPE("error.upload.file.unknown.type");

        private final String messageCode;
    }
}
