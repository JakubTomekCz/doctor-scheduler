package com.github.jakubtomekcz.doctorscheduler.controller;

import com.github.jakubtomekcz.doctorscheduler.error.UiMessageException;
import com.github.jakubtomekcz.doctorscheduler.model.PreferenceTable;
import com.github.jakubtomekcz.doctorscheduler.model.PreferenceTableWithSchedule;
import com.github.jakubtomekcz.doctorscheduler.model.Schedule;
import com.github.jakubtomekcz.doctorscheduler.parser.PreferenceTableParserService;
import com.github.jakubtomekcz.doctorscheduler.scheduler.SchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final PreferenceTableParserService preferenceTableParserService;

    private final SchedulerService schedulerService;

    @GetMapping("/")
    public String get() {
        return "index";
    }

    @PostMapping("/")
    public ModelAndView uploadPreferenceTable(@RequestParam("fileToUpload") MultipartFile uploadedFile) {
        ModelAndView modelAndView = new ModelAndView("index");
        if (!uploadedFile.isEmpty()) {
            try {
                List<PreferenceTable> preferenceTables = preferenceTableParserService.parseMultipartFile(uploadedFile);
                List<PreferenceTableWithSchedule> schedules = preferenceTables.stream()
                        .map(this::mapPreferenceTablesToSchedules)
                        .toList();
                modelAndView.addObject("schedules", schedules);
            } catch (UiMessageException e) {
                String messageCode = e.getMessageCode().getMessageCode();
                Object[] messageParams = e.getMessageParams();
                modelAndView.addObject("isError", true);
                modelAndView.addObject("errorMessageCode", messageCode);
                modelAndView.addObject("errorMessageParams", messageParams);
            }
        }
        return modelAndView;
    }

    private PreferenceTableWithSchedule mapPreferenceTablesToSchedules(PreferenceTable preferenceTable) {
        Optional<Schedule> optionalSchedule = schedulerService.createSchedule(preferenceTable);
        return new PreferenceTableWithSchedule(preferenceTable, optionalSchedule.orElse(null));
    }
}
