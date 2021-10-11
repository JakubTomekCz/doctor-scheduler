package com.github.jakubtomekcz.doctorscheduler.controller;

import com.github.jakubtomekcz.doctorscheduler.constant.ExamplePreferenceTableFile;
import com.github.jakubtomekcz.doctorscheduler.error.UiMessageException;
import com.github.jakubtomekcz.doctorscheduler.service.PreferenceTableParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MainController.class)
class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PreferenceTableParser preferenceTableParser;

    @Test
    void getIndex() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @EnumSource(ExamplePreferenceTableFile.class)
    void uploadPreferenceTable(ExamplePreferenceTableFile file) throws Exception {
        byte[] bytes = readBytesOfExampleFile(file);
        var multipartFile = new MockMultipartFile("fileToUpload", file.getFilename(), file.getContentType(), bytes);

        mockMvc.perform(multipart("/").file(multipartFile))
                .andExpect(status().isOk());
    }

    @Test
    void uploadPreferenceTableErrorMessageWithParameters() throws Exception {
        var multipartFile = new MockMultipartFile("fileToUpload", "a.txt", "text/txt", "a".getBytes());
        when(preferenceTableParser.buildPreferenceTable(multipartFile))
                .thenThrow(new UiMessageException(UiMessageException.MessageCode.UPLOAD_FILE_TOO_BIG, "20KB", "10KB"));

        mockMvc.perform(multipart("/").file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("The given file cannot be processed because "
                        + "it has a size of 20KB which exceeds the maximum allowed size of 10KB.")));
    }

    private byte[] readBytesOfExampleFile(ExamplePreferenceTableFile file) throws IOException {
        Path path = Path.of("datasets", file.getFilename());
        ClassPathResource resource = new ClassPathResource(path.toString());
        return Files.readAllBytes(resource.getFile().toPath());
    }
}
