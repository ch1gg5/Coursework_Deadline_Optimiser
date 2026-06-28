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
public class AcademicModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String moduleCode;
    private int credits;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @JsonIgnore //to avoid infinite json recursion
    @OneToMany(mappedBy = "module")
    private List<Coursework> courseworks;
}
