package com.chiggs.coursework_deadline_optimiser.controller;

import com.chiggs.coursework_deadline_optimiser.dto.StudentRequest;
import com.chiggs.coursework_deadline_optimiser.model.Student;
import com.chiggs.coursework_deadline_optimiser.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentController {

    @Autowired
    private StudentService service;

    @GetMapping("/students")
    public List<Student> getAllStudents(){
        return service.getAllStudents();
    }

    @GetMapping("/students/{id}")
    public Student getStudentById(@PathVariable Long id){
        return service.getStudentById(id);
    }

    @PostMapping("/students")
    public void addStudent(@RequestBody StudentRequest student){
        service.addStudent(student);
    }

    @PutMapping("/students/{id}")
    public void updateStudent(@PathVariable Long id, @RequestBody StudentRequest request){
        service.updateStudent(id, request );
    }

    @DeleteMapping("/students/{id}")
    public void deleteStudentById(@PathVariable Long id){
        service.deleteStudentById(id);
    }

}
