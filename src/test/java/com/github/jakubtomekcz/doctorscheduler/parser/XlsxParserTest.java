package com.github.jakubtomekcz.doctorscheduler.parser;


import com.github.jakubtomekcz.doctorscheduler.constant.ExamplePreferenceTableFile;
import com.github.jakubtomekcz.doctorscheduler.model.PreferenceTable;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

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
                .isNull();
    }

}
