package com.chiggs.coursework_deadline_optimiser.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    private Integer maxHoursPerDay;

    @JsonIgnore //to avoid infinite json recursion
    @OneToMany(mappedBy = "student")
    private List<Coursework> courseworks;
}
