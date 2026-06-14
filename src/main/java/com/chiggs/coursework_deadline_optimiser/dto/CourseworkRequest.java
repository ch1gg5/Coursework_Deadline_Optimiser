package com.chiggs.coursework_deadline_optimiser.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class CourseworkRequest {

    private String title;
    private LocalDate deadline;
    private Integer weighting;
    private Integer estimatedHours;
    private Long studentId;
    private Long moduleId;
}
