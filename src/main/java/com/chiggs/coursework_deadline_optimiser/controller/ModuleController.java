package com.chiggs.coursework_deadline_optimiser.controller;

import com.chiggs.coursework_deadline_optimiser.dto.ModuleRequest;
import com.chiggs.coursework_deadline_optimiser.model.AcademicModule;
import com.chiggs.coursework_deadline_optimiser.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ModuleController {

    @Autowired
    private ModuleService service;

    @GetMapping("/modules")
    public List<AcademicModule> getAllModules(){
        return service.getAllModules();
    }

    @GetMapping("/modules/{id}")
    public AcademicModule getModuleById(@PathVariable Long id){
        return service.getModuleById(id);
    }

    @PostMapping("/modules")
    public void addModule(@RequestBody ModuleRequest module){
        service.addModule(module);
    }

    @PutMapping("/modules/{id}")
    public void updateModule(@PathVariable Long id, @RequestBody AcademicModule request){
        service.updateModule(id, request);
    }

    @DeleteMapping("/modules/{id}")
    public void deleteModule(@PathVariable Long id){
        service.deleteModuleById(id);
    }


}
