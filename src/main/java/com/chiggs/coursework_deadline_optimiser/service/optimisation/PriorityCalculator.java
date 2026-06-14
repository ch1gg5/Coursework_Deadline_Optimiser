package com.chiggs.coursework_deadline_optimiser.service.optimisation;

import com.chiggs.coursework_deadline_optimiser.model.Coursework;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class PriorityCalculator {

    public double calculatePriority(Coursework cw){

        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), cw.getDeadline());

        if (daysLeft <= 0){
            return Double.MAX_VALUE;
        }

        double urgencyFactor = 1.0 / daysLeft;

        double finalCalc = cw.getWeighting() * cw.getDifficulty() * cw.getEstimatedHours() * urgencyFactor;

        return finalCalc;
    }
}
