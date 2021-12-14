package com.github.jakubtomekcz.doctorscheduler.parser;


import com.github.jakubtomekcz.doctorscheduler.constant.ExamplePreferenceTableFile;
import com.github.jakubtomekcz.doctorscheduler.error.UiMessageException;
import com.github.jakubtomekcz.doctorscheduler.model.Person;
import com.github.jakubtomekcz.doctorscheduler.model.PreferenceTable;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import static com.github.jakubtomekcz.doctorscheduler.constant.PersonAndDateTestConstants.date;
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

    private static final Person DOC = new Person("Doc");
    private static final Person GRUMPY = new Person("Grumpy");
    private static final Person HAPPY = new Person("Happy");
    private static final Person SLEEPY = new Person("Sleepy");
    private static final Person BASHFUL = new Person("Bashful");
    private static final Person SNEEZY = new Person("Sneezy");
    private static final Person DOPEY = new Person("Dopey");

    private static final Person PROFA = new Person("Prófa");
    private static final Person REJPAL = new Person("Rejpal");
    private static final Person STISTKO = new Person("Štístko");
    private static final Person DRIMAL = new Person("Dřímal");
    private static final Person STYDLIN = new Person("Stydlín");
    private static final Person KEJCHAL = new Person("Kejchal");
    private static final Person SMUDLA = new Person("Šmudla");

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
                            .containsExactly(DOC, GRUMPY, HAPPY, SLEEPY, BASHFUL, SNEEZY, DOPEY);
                    assertThat(table.getPreference(DOC, date("2021-09-01"))).isEqualTo(NO);
                    assertThat(table.getPreference(GRUMPY, date("2021-09-02"))).isEqualTo(YES);
                    assertThat(table.getPreference(SLEEPY, date("2021-09-04"))).isEqualTo(PREFER);
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
                            .containsExactly(PROFA, REJPAL, STISTKO, DRIMAL, STYDLIN, KEJCHAL, SMUDLA);
                    assertThat(table.getPreference(PROFA, date("2021-09-01"))).isEqualTo(NO);
                    assertThat(table.getPreference(REJPAL, date("2021-09-02"))).isEqualTo(YES);
                    assertThat(table.getPreference(DRIMAL, date("2021-09-04"))).isEqualTo(PREFER);
                });
    }

    @Test
    void parseXlsxCz2TabsExampleFile() throws IOException {
        MultipartFile multipartFile = mockMultipartFile(ExamplePreferenceTableFile.XSLX_2TAB_CZ.getFilename());

        PreferenceTable actualResult = xlsxParser.parseMultipartFile(multipartFile);

        assertThat(actualResult)
                .isNotNull()
                .satisfies(table -> assertThat(table.getName()).isEqualTo("Mladší lékaři"));
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
