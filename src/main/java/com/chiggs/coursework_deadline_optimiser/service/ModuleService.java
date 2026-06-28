package com.chiggs.coursework_deadline_optimiser.service;

import com.chiggs.coursework_deadline_optimiser.dto.ModuleRequest;
import com.chiggs.coursework_deadline_optimiser.repo.ModuleRepo;
import com.chiggs.coursework_deadline_optimiser.model.AcademicModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleService {

    @Autowired
    private ModuleRepo repo;

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;


    public List<AcademicModule> getAllModules(){
        return repo.findByUser(userService.getCurrentUser());
    }

    public AcademicModule getModuleById(Long id){
        AcademicModule module = repo.findById(id).orElse(null);
        if (module != null && !module.getUser().getStudent().equals(studentService.getCurrentStudent())) {
            throw new RuntimeException("Access denied");
        }
        return module;
    }
    
    public void addModule(ModuleRequest request){
        AcademicModule module = new AcademicModule();
        module.setName(request.getName());
        module.setModuleCode(request.getModuleCode());
        module.setCredits(request.getCredits());
        module.setUser(studentService.getCurrentStudent().getUser());

        repo.save(module);
    }
    
    public void updateModule(Long id, AcademicModule module){
        AcademicModule existing = getModuleById(id);
        if (existing == null) {
            throw new RuntimeException("Module not found: " + id);
        }

        existing.setName(module.getName());
        existing.setModuleCode(module.getModuleCode());
        existing.setCredits(module.getCredits());

        repo.save(existing);
    }
    
    public void deleteModuleById(Long id){
        AcademicModule existing = getModuleById(id);
        if (existing != null) {
            repo.delete(existing);
        }
    }

}
