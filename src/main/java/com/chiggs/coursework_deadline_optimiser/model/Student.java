package com.chiggs.coursework_deadline_optimiser.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {

    @Id
    private String email;

    private String name;

    private Integer maxHoursPerDay;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;
    
}
