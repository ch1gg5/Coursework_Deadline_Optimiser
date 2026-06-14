package com.chiggs.coursework_deadline_optimiser.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CourseworkRequest {

    private String title;
    private LocalDate deadline;
    private Integer weighting;
    private Integer difficulty;
    private Integer estimatedHours;
    private Long studentId;
    private Long moduleId;
}
