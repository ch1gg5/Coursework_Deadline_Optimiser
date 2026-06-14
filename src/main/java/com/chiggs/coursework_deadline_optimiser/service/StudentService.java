package com.chiggs.coursework_deadline_optimiser.service;

import com.chiggs.coursework_deadline_optimiser.dto.StudentRequest;
import com.chiggs.coursework_deadline_optimiser.model.Student;
import com.chiggs.coursework_deadline_optimiser.repo.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepo repo;

    public List<Student> getAllStudents(){
        return repo.findAll();
    }

    public Student getStudentById(Long id){
        return repo.findById(id).orElse(null);
    }

    public void addStudent(StudentRequest request){
        Student student = new Student();
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        repo.save(student);
    }

    public void updateStudent(Long id, StudentRequest request){
        Student existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found: " + id));

        existing.setName(request.getName());
        existing.setEmail(request.getEmail());

        repo.save(existing);
    }

    public void deleteStudentById(Long id){
        repo.deleteById(id);
    }
}
