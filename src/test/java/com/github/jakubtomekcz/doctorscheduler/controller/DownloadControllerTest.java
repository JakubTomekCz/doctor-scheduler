package com.github.jakubtomekcz.doctorscheduler.controller;

import com.github.jakubtomekcz.doctorscheduler.constant.ExamplePreferenceTableFile;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DownloadController.class)
class DownloadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @EnumSource(ExamplePreferenceTableFile.class)
    void downloadExamplePreferenceTableFile(ExamplePreferenceTableFile file) throws Exception {
        Path path = Path.of("datasets", file.getFilename());
        ClassPathResource resource = new ClassPathResource(path.toString());
        byte[] expectedContent = Files.readAllBytes(resource.getFile().toPath());

        MvcResult result = mockMvc.perform(get("/download/" + file.getFilename()))
                .andExpect(status().isOk())
                .andExpect(header().string(CONTENT_TYPE, file.getContentType()))
                .andExpect(header().string(CONTENT_DISPOSITION, format("inline; filename=\"%s\"", file.getFilename())))
                .andExpect(header().longValue(CONTENT_LENGTH, expectedContent.length))
                .andReturn();

        byte[] actualContent = result.getResponse().getContentAsByteArray();

        assertThat(actualContent).isEqualTo(expectedContent);
    }
}
