package com.github.jakubtomekcz.doctorscheduler.parser;


import com.github.jakubtomekcz.doctorscheduler.constant.ExamplePreferenceTableFile;
import com.github.jakubtomekcz.doctorscheduler.error.UiMessageException;
import com.github.jakubtomekcz.doctorscheduler.schedule.PreferenceTable;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.NO;
import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.PREFER;
import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.YES;
import static com.github.jakubtomekcz.doctorscheduler.error.UiMessageException.MessageCode.ERROR_READING_XLSX_FILE;
import static com.github.jakubtomekcz.doctorscheduler.error.UiMessageException.MessageCode.XLSX_FILE_DATE_EXPECTED;
import static com.github.jakubtomekcz.doctorscheduler.error.UiMessageException.MessageCode.XLSX_FILE_PERSON_NAME_TOO_LONG;
import static com.github.jakubtomekcz.doctorscheduler.error.UiMessageException.MessageCode.XLSX_FILE_PREFERENCE_EXPECTED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MockitoSettings
class XlsxParserTest {

    private final XlsxParser xlsxParser = new XlsxParser();

    @Test
    void parseXlsxEnExampleFile() throws IOException {
        MultipartFile multipartFile = mockMultipartFile(ExamplePreferenceTableFile.XSLX_EN.getFilename());

        PreferenceTable actualResult = xlsxParser.parseMultipartFile(multipartFile);

        assertThat(actualResult)
                .isNotNull()
                .satisfies(table -> {
                    assertThat(table.getDates()).hasSize(30);
                    assertThat(table.getPersons())
                            .containsExactly("Doc", "Grumpy", "Happy", "Sleepy", "Bashful", "Sneezy", "Dopey");
                    assertThat(table.getPreference("Doc", "Wed 01 Sept 2021")).isEqualTo(NO);
                    assertThat(table.getPreference("Grumpy", "Thu 02 Sept 2021")).isEqualTo(YES);
                    assertThat(table.getPreference("Sleepy", "Sat 04 Sept 2021")).isEqualTo(PREFER);
                });
    }

    @Test
    void parseXlsxCzExampleFile() throws IOException {
        MultipartFile multipartFile = mockMultipartFile(ExamplePreferenceTableFile.XSLX_CZ.getFilename());

        PreferenceTable actualResult = xlsxParser.parseMultipartFile(multipartFile);

        assertThat(actualResult)
                .isNotNull()
                .satisfies(table -> {
                    assertThat(table.getDates()).hasSize(30);
                    assertThat(table.getPersons())
                            .containsExactly("Prófa", "Rejpal", "Štístko", "Dřímal", "Stydlín", "Kejchal", "Šmudla");
                    assertThat(table.getPreference("Prófa", "Wed 01 Sept 2021")).isEqualTo(NO);
                    assertThat(table.getPreference("Rejpal", "Thu 02 Sept 2021")).isEqualTo(YES);
                    assertThat(table.getPreference("Dřímal", "Sat 04 Sept 2021")).isEqualTo(PREFER);
                });
    }

    @Test
    void errorOpeningInputStream() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getInputStream()).thenThrow(IOException.class);
        assertThatThrownBy(() -> xlsxParser.parseMultipartFile(multipartFile))
                .isInstanceOf(UiMessageException.class)
                .hasFieldOrPropertyWithValue("messageCode", ERROR_READING_XLSX_FILE);
    }

    @Test
    void emptyFile() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getInputStream()).thenReturn(InputStream.nullInputStream());
        assertThatThrownBy(() -> xlsxParser.parseMultipartFile(multipartFile))
                .isInstanceOf(UiMessageException.class)
                .hasFieldOrPropertyWithValue("messageCode", ERROR_READING_XLSX_FILE);
    }

    @Test
    void wrongFileFormat() throws IOException {
        MultipartFile multipartFile = mockMultipartFile("preference-table-example-en.csv");
        assertThatThrownBy(() -> xlsxParser.parseMultipartFile(multipartFile))
                .isInstanceOf(UiMessageException.class)
                .hasFieldOrPropertyWithValue("messageCode", ERROR_READING_XLSX_FILE);
    }

    @Test
    void wrongDateFormat() throws IOException {
        MultipartFile multipartFile = mockMultipartFile("preference-table-wrong-date-format.xlsx");
        assertThatThrownBy(() -> xlsxParser.parseMultipartFile(multipartFile))
                .isInstanceOf(UiMessageException.class)
                .hasFieldOrPropertyWithValue("messageCode", XLSX_FILE_DATE_EXPECTED);
    }

    @Test
    void nameTooLong() throws IOException {
        MultipartFile multipartFile = mockMultipartFile("preference-table-name-too-long.xlsx");
        assertThatThrownBy(() -> xlsxParser.parseMultipartFile(multipartFile))
                .isInstanceOf(UiMessageException.class)
                .hasFieldOrPropertyWithValue("messageCode", XLSX_FILE_PERSON_NAME_TOO_LONG);
    }

    @Test
    void wrongPreferenceType() throws IOException {
        MultipartFile multipartFile = mockMultipartFile("preference-table-wrong-preference-type.xlsx");
        assertThatThrownBy(() -> xlsxParser.parseMultipartFile(multipartFile))
                .isInstanceOf(UiMessageException.class)
                .hasFieldOrPropertyWithValue("messageCode", XLSX_FILE_PREFERENCE_EXPECTED);
    }

    private MultipartFile mockMultipartFile(String filename) throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        Path path = Path.of("datasets", filename);
        ClassPathResource resource = new ClassPathResource(path.toString());
        when(multipartFile.getInputStream()).thenReturn(resource.getInputStream());
        return multipartFile;
    }
}
