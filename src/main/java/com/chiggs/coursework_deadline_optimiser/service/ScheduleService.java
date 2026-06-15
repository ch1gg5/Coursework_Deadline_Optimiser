package com.chiggs.coursework_deadline_optimiser.service;

import com.chiggs.coursework_deadline_optimiser.model.Coursework;
import com.chiggs.coursework_deadline_optimiser.model.StudySession;
import com.chiggs.coursework_deadline_optimiser.repo.CourseworkRepo;
import com.chiggs.coursework_deadline_optimiser.repo.StudySessionRepo;
import com.chiggs.coursework_deadline_optimiser.service.optimisation.PriorityCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ScheduleService {

    @Autowired
    private StudySessionRepo studySessionRepo;

    @Autowired
    private CourseworkRepo courseworkRepo;

    @Autowired
    private PriorityCalculator pc;

    public List<StudySession> generateSchedule() {

        studySessionRepo.deleteAll();

        List<Coursework> courseworks = courseworkRepo.findAll();

        LocalDate today = LocalDate.now();
        int defaultMaxHoursPerDay = 4;
        int planningDays = 60;

        List<StudySession> schedule = new ArrayList<>();

        // track remaining work per coursework
        Map<Long, Double> remainingWork = new HashMap<>();
        for (Coursework cw : courseworks) {
            remainingWork.put(cw.getId(), (double) cw.getEstimatedHours());
        }

        // Prepare mapping from coursework -> student and student capacity config
        Map<Long, Long> courseworkToStudent = new HashMap<>();
        Map<Long, Integer> studentCapacityConfig = new HashMap<>();

        for (Coursework cw : courseworks) {
            if (cw.getStudent() != null) {
                courseworkToStudent.put(cw.getId(), cw.getStudent().getId());
                Integer cap = cw.getStudent().getMaxHoursPerDay();
                studentCapacityConfig.put(cw.getStudent().getId(), cap == null ? defaultMaxHoursPerDay : cap);
            } else {
                courseworkToStudent.put(cw.getId(), null);
            }
        }

        for (int d = 0; d < planningDays; d++) {

            LocalDate date = today.plusDays(d);

            // Group active coursework per student
            Map<Long, List<Coursework>> activeByStudent = new HashMap<>();

            for (Coursework cw : courseworks) {

                double remaining = remainingWork.get(cw.getId());
                if (remaining <= 0) continue;
                if (date.isAfter(cw.getDeadline())) continue;

                Long studentId = courseworkToStudent.get(cw.getId());
                activeByStudent.computeIfAbsent(studentId, k -> new ArrayList<>()).add(cw);
            }

            // For each student, allocate up to their capacity independently
            for (Map.Entry<Long, List<Coursework>> entry : activeByStudent.entrySet()) {

                Long studentId = entry.getKey();
                List<Coursework> activeList = entry.getValue();
                if (activeList.isEmpty()) continue;

                int dailyCapacity = (studentId == null) ? defaultMaxHoursPerDay
                        : studentCapacityConfig.getOrDefault(studentId, defaultMaxHoursPerDay);

                // STEP 1: calculate weights for this student's active coursework
                Map<Long, Double> weights = new HashMap<>();
                double totalWeight = 0;

                for (Coursework cw : activeList) {

                    double remaining = remainingWork.get(cw.getId());
                    double priority = pc.calculatePriority(cw);

                    double weight = priority * remaining;

                    weights.put(cw.getId(), weight);
                    totalWeight += weight;
                }

                if (totalWeight <= 0) continue;

                // STEP 2: allocate hours among this student's active coursework
                for (Coursework cw : activeList) {

                    if (dailyCapacity <= 0) break;

                    double weight = weights.get(cw.getId());
                    double share = weight / totalWeight;

                    double allocated = share * dailyCapacity;

                    // IMPORTANT FIXES
                    allocated = Math.min(allocated, remainingWork.get(cw.getId()));
                    allocated = Math.min(allocated, dailyCapacity);

                    // prevent zero-hour allocations
                    allocated = Math.max(allocated, 1);

                    int finalAllocated = (int) Math.round(allocated);

                    if (finalAllocated <= 0) continue;

                    StudySession session = new StudySession();
                    Coursework allocatedCw = courseworkRepo.findById(cw.getId()).orElse(null);

                    session.setDate(date);
                    session.setCoursework(allocatedCw);
                    session.setTask(cw.getTitle());
                    session.setHoursAllocated(finalAllocated);

                    studySessionRepo.save(session);

                    schedule.add(session);

                    remainingWork.put(
                            cw.getId(),
                            remainingWork.get(cw.getId()) - finalAllocated
                    );

                    dailyCapacity -= finalAllocated;
                }
            }
        }

        return schedule;
    }

    public List<StudySession> getSchedule() {
        return studySessionRepo.findAll();
    }

}
