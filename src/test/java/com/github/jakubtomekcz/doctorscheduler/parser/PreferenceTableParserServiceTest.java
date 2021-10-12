package com.github.jakubtomekcz.doctorscheduler.parser;


import com.github.jakubtomekcz.doctorscheduler.error.UiMessageException;
import com.github.jakubtomekcz.doctorscheduler.model.PreferenceTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import static com.github.jakubtomekcz.doctorscheduler.error.UiMessageException.MessageCode.UPLOAD_FILE_TOO_BIG;
import static com.github.jakubtomekcz.doctorscheduler.error.UiMessageException.MessageCode.UPLOAD_FILE_UNKNOWN_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@MockitoSettings
class PreferenceTableParserServiceTest {

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private PreferenceTableParserFactory parserFactory;

    @Mock
    private PreferenceTableParser parser;

    @Mock
    private PreferenceTable preferenceTable;

    private PreferenceTableParserService parserService;

    @BeforeEach
    void init() {
        parserService = new PreferenceTableParserService(DataSize.parse("200KB"), parserFactory);
    }

    @Test
    void parseMultipartFile() {
        when(multipartFile.getOriginalFilename()).thenReturn("file.xlsx");
        when(parserFactory.getParserForFileExtension("xlsx")).thenReturn(parser);
        when(parser.parseMultipartFile(multipartFile)).thenReturn(preferenceTable);

        PreferenceTable actualResult = parserService.parseMultipartFile(multipartFile);

        assertThat(actualResult).isSameAs(preferenceTable);
    }

    @Test
    void unknownFileExtension() {
        when(multipartFile.getOriginalFilename()).thenReturn("file.png");
        when(parserFactory.getParserForFileExtension("png")).thenThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> parserService.parseMultipartFile(multipartFile))
                .isInstanceOf(UiMessageException.class)
                .hasFieldOrPropertyWithValue("messageCode", UPLOAD_FILE_UNKNOWN_TYPE);
    }

    @Test
    void missingFileExtension() {
        when(multipartFile.getOriginalFilename()).thenReturn("");
        when(parserFactory.getParserForFileExtension("")).thenThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> parserService.parseMultipartFile(multipartFile))
                .isInstanceOf(UiMessageException.class)
                .hasFieldOrPropertyWithValue("messageCode", UPLOAD_FILE_UNKNOWN_TYPE);
    }

    @Test
    void missingFileName() {
        when(multipartFile.getOriginalFilename()).thenReturn(null);
        when(parserFactory.getParserForFileExtension("")).thenThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> parserService.parseMultipartFile(multipartFile))
                .isInstanceOf(UiMessageException.class)
                .hasFieldOrPropertyWithValue("messageCode", UPLOAD_FILE_UNKNOWN_TYPE);
    }

    @Test
    void fileTooBig() {
        when(multipartFile.getSize()).thenReturn(DataSize.ofGigabytes(1).toBytes());

        assertThatThrownBy(() -> parserService.parseMultipartFile(multipartFile))
                .isInstanceOf(UiMessageException.class)
                .hasFieldOrPropertyWithValue("messageCode", UPLOAD_FILE_TOO_BIG);
    }
}
