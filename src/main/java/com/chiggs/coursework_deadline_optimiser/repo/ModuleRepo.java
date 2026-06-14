package com.chiggs.coursework_deadline_optimiser.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.chiggs.coursework_deadline_optimiser.model.AcademicModule;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepo extends JpaRepository<AcademicModule,Long> {
}
