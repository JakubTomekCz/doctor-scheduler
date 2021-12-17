package com.github.jakubtomekcz.doctorscheduler.controller;

import com.github.jakubtomekcz.doctorscheduler.error.UiMessageException;
import com.github.jakubtomekcz.doctorscheduler.model.PreferenceTable;
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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
                Map<PreferenceTable, Schedule> schedules = new LinkedHashMap<>();
                for (PreferenceTable preferenceTable : preferenceTables) {
                    Optional<Schedule> optionalSchedule = schedulerService.createSchedule(preferenceTable);
                    optionalSchedule.ifPresent(schedule -> schedules.put(preferenceTable, schedule));
                }
                modelAndView.addObject("preferenceTables", preferenceTables);
                modelAndView.addObject("schedules", schedules);

                // TODO remove
                PreferenceTable preferenceTable = preferenceTables.size() > 0 ? preferenceTables.get(0) : null;
                modelAndView.addObject("preferenceTable", preferenceTable);
                Schedule schedule = schedules.get(preferenceTable);
                modelAndView.addObject("schedule", schedule);
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
}
