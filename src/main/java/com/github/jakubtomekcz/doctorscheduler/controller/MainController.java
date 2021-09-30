package com.github.jakubtomekcz.doctorscheduler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String get() {
        return "index";
    }

}
