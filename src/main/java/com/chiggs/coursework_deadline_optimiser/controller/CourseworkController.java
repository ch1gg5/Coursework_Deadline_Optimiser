package com.chiggs.coursework_deadline_optimiser.controller;

import com.chiggs.coursework_deadline_optimiser.dto.CourseworkRequest;
import com.chiggs.coursework_deadline_optimiser.model.Coursework;
import com.chiggs.coursework_deadline_optimiser.service.CourseworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CourseworkController {

    @Autowired
    private CourseworkService service;

    @GetMapping("/courseworks")
    public List<Coursework> getAllCourseworks(){
        return service.getAllCoursework();
    }

    @GetMapping("/courseworks/{id}")
    public Coursework getCourseworkById(@PathVariable Long id){
        return service.getCourseById(id);
    }

    @PostMapping("/courseworks")
    public void addCourse(@RequestBody CourseworkRequest coursework){
        service.addCoursework(coursework);
    }

    @PutMapping("/courseworks/{id}")
    public void updateCourse(@PathVariable Long id, @RequestBody CourseworkRequest request ){
        service.updateCoursework(id, request);
    }

    @DeleteMapping("/courseworks/{id}")
    public void deleteCourse(@PathVariable Long id){
        service.deleteCourseById(id);
    }


}
