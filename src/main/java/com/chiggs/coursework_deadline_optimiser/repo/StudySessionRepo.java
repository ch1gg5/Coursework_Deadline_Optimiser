package com.chiggs.coursework_deadline_optimiser.repo;

import com.chiggs.coursework_deadline_optimiser.model.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudySessionRepo extends JpaRepository<StudySession, Long> {
}
