package com.github.jakubtomekcz.doctorscheduler.parser;


import com.github.jakubtomekcz.doctorscheduler.constant.ExamplePreferenceTableFile;
import com.github.jakubtomekcz.doctorscheduler.model.PreferenceTable;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.NO;
import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.PREFER;
import static com.github.jakubtomekcz.doctorscheduler.constant.PreferenceType.YES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MockitoSettings
class XlsxParserTest {

    private final XlsxParser xlsxParser = new XlsxParser();

    @Test
    void parseXlsxEnExampleFile() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        Path path = Path.of("datasets", ExamplePreferenceTableFile.XSLX_EN.getFilename());
        ClassPathResource resource = new ClassPathResource(path.toString());
        when(multipartFile.getInputStream()).thenReturn(resource.getInputStream());

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
        MultipartFile multipartFile = mock(MultipartFile.class);
        Path path = Path.of("datasets", ExamplePreferenceTableFile.XSLX_CZ.getFilename());
        ClassPathResource resource = new ClassPathResource(path.toString());
        when(multipartFile.getInputStream()).thenReturn(resource.getInputStream());

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
}
