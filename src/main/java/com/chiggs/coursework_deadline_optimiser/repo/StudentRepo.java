package com.chiggs.coursework_deadline_optimiser.repo;

import com.chiggs.coursework_deadline_optimiser.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepo extends JpaRepository<Student,Long> {
}
