package com.chiggs.coursework_deadline_optimiser.controller;

import com.chiggs.coursework_deadline_optimiser.model.StudySession;
import com.chiggs.coursework_deadline_optimiser.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ScheduleController {

    @Autowired
    private ScheduleService service;

    @GetMapping("/schedule/generate")
    public List<StudySession> generateSchedule() {
         return service.generateSchedule();
    }

    @GetMapping("/schedule")
    public List<StudySession> getSchedule() {
        return service.getSchedule();
    }

}
