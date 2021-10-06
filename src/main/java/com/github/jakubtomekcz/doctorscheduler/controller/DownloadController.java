package com.github.jakubtomekcz.doctorscheduler.controller;

import com.github.jakubtomekcz.doctorscheduler.constant.ExamplePreferenceTableFile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.Path;

import static java.lang.String.format;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@Controller
public class DownloadController {

    @GetMapping(value = "download/{file}", produces = "application/xlsx")
    public @ResponseBody
    Resource downloadExamplePreferenceTable(
            @PathVariable ExamplePreferenceTableFile file,
            HttpServletResponse response) {
        Path path = Path.of("datasets", file.getFilename());
        ClassPathResource resource = new ClassPathResource(path.toString());
        response.setHeader(CONTENT_DISPOSITION, format("inline; filename=\"%s\"", resource.getFilename()));
        response.setHeader(CONTENT_TYPE, file.getContentType());
        return resource;
    }

}
