package com.chiggs.coursework_deadline_optimiser;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Coursework {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Date deadline;
    private int estimatedHours;
    private int weighting;
    private int difficulty;
    private int progress;

    @ManyToOne
    private Module module;
}
