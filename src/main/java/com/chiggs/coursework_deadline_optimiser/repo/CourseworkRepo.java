package com.chiggs.coursework_deadline_optimiser.repo;

import com.chiggs.coursework_deadline_optimiser.model.Coursework;
import com.chiggs.coursework_deadline_optimiser.model.Student;
import com.chiggs.coursework_deadline_optimiser.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseworkRepo  extends JpaRepository<Coursework,Long> {
    List<Coursework> findByUser(Users user);
}
