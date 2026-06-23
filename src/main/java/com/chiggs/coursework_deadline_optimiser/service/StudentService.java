package com.chiggs.coursework_deadline_optimiser.service;

import com.chiggs.coursework_deadline_optimiser.dto.StudentRequest;
import com.chiggs.coursework_deadline_optimiser.model.Student;
import com.chiggs.coursework_deadline_optimiser.model.Users;
import com.chiggs.coursework_deadline_optimiser.repo.StudentRepo;
import com.chiggs.coursework_deadline_optimiser.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepo repo;

    @Autowired
    private UserRepo userRepo;

    public Student getCurrentStudent() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = userRepo.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return repo.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Student not found for email: " + user.getEmail()));
    }

    public List<Student> getAllStudents(){
        return Collections.singletonList(getCurrentStudent());
    }

    public Student getStudentById(String id){
        Student current = getCurrentStudent();
        if (!current.getEmail().equals(id)) {
            throw new RuntimeException("Access denied");
        }
        return current;
    }

    public StudentRequest addStudent(StudentRequest request){
        // In this new model, students are created during registration. 
        // But if we need to support this:
        Student student = new Student();
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setMaxHoursPerDay(request.getMaxHoursPerDay());

        Student newStudent = repo.save(student);

        StudentRequest studentResponse = new StudentRequest();
        studentResponse.setName(newStudent.getName());
        studentResponse.setEmail(newStudent.getEmail());
        studentResponse.setMaxHoursPerDay(newStudent.getMaxHoursPerDay());

        return studentResponse;
    }

    public void updateStudent(String id, StudentRequest request){
        Student current = getCurrentStudent();
        if (!current.getEmail().equals(id)) {
             throw new RuntimeException("Access denied");
        }

        current.setName(request.getName());
        current.setMaxHoursPerDay(request.getMaxHoursPerDay());

        repo.save(current);
    }

    public void deleteStudentById(String id){
        Student current = getCurrentStudent();
        if (!current.getEmail().equals(id)) {
            throw new RuntimeException("Access denied");
        }
        repo.deleteById(id);
    }
}
