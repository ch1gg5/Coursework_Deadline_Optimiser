package com.chiggs.coursework_deadline_optimiser.service;

import com.chiggs.coursework_deadline_optimiser.model.Coursework;
import com.chiggs.coursework_deadline_optimiser.model.StudySession;
import com.chiggs.coursework_deadline_optimiser.repo.CourseworkRepo;
import com.chiggs.coursework_deadline_optimiser.service.optimisation.PriorityCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ScheduleService {

    @Autowired
    private CourseworkRepo courseworkRepo;

    @Autowired
    private PriorityCalculator pc;

    public List<StudySession> generateSchedule() {

        List<Coursework> courseworks = courseworkRepo.findAll();

        LocalDate today = LocalDate.now();
        int maxHoursPerDay = 4;
        int planningDays = 60;

        List<StudySession> schedule = new ArrayList<>();

        // track remaining work per coursework
        Map<Long, Double> remainingWork = new HashMap<>();
        for (Coursework cw : courseworks) {
            remainingWork.put(cw.getId(), (double) cw.getEstimatedHours());
        }

        for (int d = 0; d < planningDays; d++) {

            LocalDate date = today.plusDays(d);
            int dailyCapacity = maxHoursPerDay;

            // STEP 1: get active coursework
            List<Coursework> active = new ArrayList<>();

            for (Coursework cw : courseworks) {

                double remaining = remainingWork.get(cw.getId());

                if (remaining <= 0) continue;

                if (date.isAfter(cw.getDeadline())) continue;

                active.add(cw);
            }

            if (active.isEmpty()) continue;

            // STEP 2: calculate weights
            Map<Long, Double> weights = new HashMap<>();
            double totalWeight = 0;

            for (Coursework cw : active) {

                double remaining = remainingWork.get(cw.getId());
                double priority = pc.calculatePriority(cw);

                double weight = priority * remaining;

                weights.put(cw.getId(), weight);
                totalWeight += weight;
            }

            // STEP 3: allocate hours
            for (Coursework cw : active) {

                if (dailyCapacity <= 0) break;

                double weight = weights.get(cw.getId());

                double share = weight / totalWeight;

                double allocated = share * maxHoursPerDay;

                // IMPORTANT FIXES
                allocated = Math.min(allocated, remainingWork.get(cw.getId()));
                allocated = Math.min(allocated, dailyCapacity);

                // prevent zero-hour allocations
                allocated = Math.max(allocated, 1);

                int finalAllocated = (int) Math.round(allocated);

                if (finalAllocated <= 0) continue;

                StudySession session = new StudySession();
                session.setDate(date);
                session.setCourseworkId(cw.getId());
                session.setTask(cw.getTitle());
                session.setHoursAllocated(finalAllocated);

                schedule.add(session);

                remainingWork.put(
                        cw.getId(),
                        remainingWork.get(cw.getId()) - finalAllocated
                );

                dailyCapacity -= finalAllocated;
            }
        }

        return schedule;
    }

}
