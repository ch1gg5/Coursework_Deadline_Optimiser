package com.chiggs.coursework_deadline_optimiser.service;

import com.chiggs.coursework_deadline_optimiser.dto.CourseworkRequest;
import com.chiggs.coursework_deadline_optimiser.model.AcademicModule;
import com.chiggs.coursework_deadline_optimiser.model.Coursework;
import com.chiggs.coursework_deadline_optimiser.model.Student;
import com.chiggs.coursework_deadline_optimiser.repo.CourseworkRepo;
import com.chiggs.coursework_deadline_optimiser.repo.ModuleRepo;
import com.chiggs.coursework_deadline_optimiser.repo.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseworkService {

    @Autowired
    private CourseworkRepo courseworkRepo;
    @Autowired
    private StudentRepo studentRepo;
    @Autowired
    private ModuleRepo moduleRepo;
    @Autowired
    private StudentService studentService;
    @Autowired
    private UserService userService;

    public List<Coursework> getAllCoursework(){
        return courseworkRepo.findByUser(userService.getCurrentUser());
    }

    public Coursework getCourseById(Long id){
        Coursework coursework = courseworkRepo.findById(id).orElse(null);
        if (coursework != null && !coursework.getUser().getStudent().equals(studentService.getCurrentStudent())) {
            throw new RuntimeException("Access denied");
        }
        return coursework;
    }

    public void addCoursework(CourseworkRequest request){
        Student student = studentService.getCurrentStudent();

        AcademicModule module =
                moduleRepo.findById(request.getModuleId()).orElse(null);
        
        if (module != null && !module.getUser().getStudent().equals(student)) {
             throw new RuntimeException("Module does not belong to student");
        }

        Coursework coursework = new Coursework();

        coursework.setTitle(request.getTitle());
        coursework.setDeadline(request.getDeadline());
        coursework.setWeighting(request.getWeighting());
        coursework.setDifficulty(request.getDifficulty());
        coursework.setEstimatedHours(
                request.getEstimatedHours());

        coursework.setUser(student.getUser());
        coursework.setModule(module);

        courseworkRepo.save(coursework);
    }

    public void updateCoursework(Long id, CourseworkRequest request){

        Coursework existing = getCourseById(id);
        if (existing == null) {
             throw new RuntimeException("Coursework not found: " + id);
        }

        Student student = studentService.getCurrentStudent();

        AcademicModule module = moduleRepo.findById(request.getModuleId())
                .orElseThrow(() -> new RuntimeException("Module not found"));

        if (!module.getUser().getStudent().equals(student)) {
            throw new RuntimeException("Module does not belong to student");
        }

        existing.setTitle(request.getTitle());
        existing.setDeadline(request.getDeadline());
        existing.setWeighting(request.getWeighting());
        existing.setEstimatedHours(request.getEstimatedHours());
        existing.setUser(student.getUser());
        existing.setModule(module);

        courseworkRepo.save(existing);

    }

    public void deleteCourseById(Long id){
        Coursework existing = getCourseById(id);
        if (existing != null) {
            courseworkRepo.delete(existing);
        }
    }
}
