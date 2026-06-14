package com.chiggs.coursework_deadline_optimiser.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudySession {

    private LocalDate date;
    private Long courseworkId;
    private String task;
    private int hoursAllocated;
}
