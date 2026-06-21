package com.chiggs.coursework_deadline_optimiser.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentRequest {
    private String name;
    private String email;
    private Integer maxHoursPerDay;
}
