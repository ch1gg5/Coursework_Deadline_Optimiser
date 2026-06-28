package com.chiggs.coursework_deadline_optimiser.repo;

import com.chiggs.coursework_deadline_optimiser.model.Student;
import com.chiggs.coursework_deadline_optimiser.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import com.chiggs.coursework_deadline_optimiser.model.AcademicModule;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepo extends JpaRepository<AcademicModule,Long> {
    List<AcademicModule> findByUser(Users user);
}
