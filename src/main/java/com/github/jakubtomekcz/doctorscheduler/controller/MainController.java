package com.github.jakubtomekcz.doctorscheduler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    @GetMapping("/")
    public String get() {
        return "index";
    }

    @PostMapping("/")
    public ModelAndView uploadPreferenceTable(@RequestParam("fileToUpload") MultipartFile fileToUpload) {
        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;
    }

}
