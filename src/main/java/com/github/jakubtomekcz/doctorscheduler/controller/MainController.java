package com.github.jakubtomekcz.doctorscheduler.controller;

import com.github.jakubtomekcz.doctorscheduler.error.UiMessageException;
import com.github.jakubtomekcz.doctorscheduler.model.PreferenceTable;
import com.github.jakubtomekcz.doctorscheduler.parser.PreferenceTableParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final PreferenceTableParserService preferenceTableParserService;

    @GetMapping("/")
    public String get() {
        return "index";
    }

    @PostMapping("/")
    public ModelAndView uploadPreferenceTable(@RequestParam("fileToUpload") MultipartFile uploadedFile) {
        ModelAndView modelAndView = new ModelAndView("index");
        if (!uploadedFile.isEmpty()) {
            try {
                PreferenceTable preferenceTable = preferenceTableParserService.parseMultipartFile(uploadedFile);
                modelAndView.addObject("preferenceTable", preferenceTable);
            } catch (UiMessageException e) {
                String messageCode = e.getMessageCode().getMessageCode();
                Object[] messageParams = e.getMessageParams();
                modelAndView.addObject("isUploadError", true);
                modelAndView.addObject("errorMessageCode", messageCode);
                modelAndView.addObject("errorMessageParams", messageParams);
            }
        }
        return modelAndView;
    }

}
