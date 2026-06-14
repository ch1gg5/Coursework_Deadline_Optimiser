package com.chiggs.coursework_deadline_optimiser.repo;

import com.chiggs.coursework_deadline_optimiser.model.Coursework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseworkRepo  extends JpaRepository<Coursework,Long> {
}
