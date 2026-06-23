package com.chiggs.coursework_deadline_optimiser.repo;

import com.chiggs.coursework_deadline_optimiser.model.Coursework;
import com.chiggs.coursework_deadline_optimiser.model.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudySessionRepo extends JpaRepository<StudySession, Long> {
    List<StudySession> findByCourseworkIn(List<Coursework> courseworks);
    void deleteByCourseworkIn(List<Coursework> courseworks);
}
