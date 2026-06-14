package com.chiggs.coursework_deadline_optimiser.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CourseworkRequest {

    private String title;
    private Date deadline;
    private Integer weighting;
    private Integer estimatedHours;
    private Long studentId;
    private Long moduleId;
}
