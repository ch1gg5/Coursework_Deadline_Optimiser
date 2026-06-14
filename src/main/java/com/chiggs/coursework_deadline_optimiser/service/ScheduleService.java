package com.chiggs.coursework_deadline_optimiser.service;

import com.chiggs.coursework_deadline_optimiser.model.Coursework;
import com.chiggs.coursework_deadline_optimiser.model.StudySession;
import com.chiggs.coursework_deadline_optimiser.repo.CourseworkRepo;
import com.chiggs.coursework_deadline_optimiser.service.optimisation.PriorityCalculator;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ScheduleService {

    @Autowired
    private CourseworkRepo courseworkRepo;

    @Autowired
    private PriorityCalculator pc;

    public List<StudySession> generateSchedule(){

        List<Coursework> courseworks = courseworkRepo.findAll();

        //sort by priority, highest first
        courseworks.sort(Comparator.comparing(pc::calculatePriority).reversed());

        List<StudySession> schedule = new ArrayList<>();
        LocalDate today = LocalDate.now();

        int maxHoursPerDay = 4;

        //track daily load
        Map<LocalDate, Integer> dailyLoad = new HashMap<>();
        for (Coursework cw : courseworks) {

            long daysLeft = ChronoUnit.DAYS.between(today, cw.getDeadline());
            if(daysLeft <=0) continue;

            double totalHours = cw.getEstimatedHours();
            double hoursPerDay = Math.ceil(totalHours / daysLeft);

            for(int i = 0; i < daysLeft; i++){

                LocalDate date = today.plusDays(i);
                int usedHours = dailyLoad.getOrDefault(date, 0);

                if (usedHours >= maxHoursPerDay) {
                    continue;
                }

                int available = maxHoursPerDay - usedHours;
                int allocated = (int) Math.min(hoursPerDay, available);

                if (allocated <= 0){
                    continue;
                }

                StudySession session = new StudySession();
                session.setDate(date);
                session.setTask(cw.getTitle());
                session.setHoursAllocated(allocated);

                schedule.add(session);
                dailyLoad.put(date, usedHours + allocated);

            }

        }

        return schedule;
    }

}
