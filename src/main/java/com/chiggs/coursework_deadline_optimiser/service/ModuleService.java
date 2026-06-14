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

    public List<AcademicModule> getAllModules(){
        return repo.findAll();
    }

    public AcademicModule getModuleById(Long id){
        return repo.findById(id).orElse(null);
    }
    
    public void addModule(ModuleRequest request){
        AcademicModule module = new AcademicModule();
        module.setName(request.getName());
        module.setModuleCode(request.getModuleCode());
        module.setCredits(request.getCredits());

        repo.save(module);
    }
    
    public void updateModule(Long id, AcademicModule module){
        AcademicModule existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found: " + id));

        existing.setName(module.getName());
        existing.setModuleCode(module.getModuleCode());
        existing.setCredits(module.getCredits());

        repo.save(existing);
    }
    
    public void deleteModuleById(Long id){
        repo.deleteById(id);
    }

}
