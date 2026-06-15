package com.chiggs.coursework_deadline_optimiser;

import com.chiggs.coursework_deadline_optimiser.model.Coursework;
import com.chiggs.coursework_deadline_optimiser.service.optimisation.PriorityCalculator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PriorityCalculatorTest {

    @Autowired
    private PriorityCalculator pc;

    @Test
    void higherWeighting_shouldIncreasePriority() {

        Coursework low = new Coursework();
        low.setWeighting(10);
        low.setDifficulty(1);
        low.setEstimatedHours(10);
        low.setDeadline(LocalDate.now().plusDays(10));

        Coursework high = new Coursework();
        high.setWeighting(80);
        high.setDifficulty(1);
        high.setEstimatedHours(10);
        high.setDeadline(LocalDate.now().plusDays(10));

        assertTrue(pc.calculatePriority(high) > pc.calculatePriority(low));
    }

    @Test
    void closerDeadline_shouldIncreasePriority() {

        Coursework far = new Coursework();
        far.setWeighting(50);
        far.setDifficulty(1);
        far.setEstimatedHours(10);
        far.setDeadline(LocalDate.now().plusDays(20));

        Coursework near = new Coursework();
        near.setWeighting(50);
        near.setDifficulty(1);
        near.setEstimatedHours(10);
        near.setDeadline(LocalDate.now().plusDays(2));

        assertTrue(pc.calculatePriority(near) > pc.calculatePriority(far));
    }

    @Test
    void overdue_shouldReturnVeryHighOrHandled() {

        Coursework overdue = new Coursework();
        overdue.setWeighting(50);
        overdue.setDifficulty(1);
        overdue.setEstimatedHours(10);
        overdue.setDeadline(LocalDate.now().minusDays(5));

        double result = pc.calculatePriority(overdue);

        assertTrue(result > 0); // or whatever rule you implemented
    }
}