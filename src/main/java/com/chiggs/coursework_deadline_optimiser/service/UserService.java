package com.chiggs.coursework_deadline_optimiser.service;

import com.chiggs.coursework_deadline_optimiser.enums.Role;
import com.chiggs.coursework_deadline_optimiser.model.Student;
import com.chiggs.coursework_deadline_optimiser.model.Users;
import com.chiggs.coursework_deadline_optimiser.repo.StudentRepo;
import com.chiggs.coursework_deadline_optimiser.repo.UserRepo;
import com.chiggs.coursework_deadline_optimiser.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, StudentRepo studentRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.studentRepo = studentRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Users register(String username, String email, String password) {
        if (userRepo.findByUsername(username) != null) {
            throw new RuntimeException("Username already taken");
        }
        if (userRepo.findByEmail(email) != null) {
            throw new RuntimeException("Email already taken");
        }

        Users user = new Users();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // BCrypt hash
        user.setRole(Role.ROLE_USER); // default role
        
        // Link with Student
        Student student = new Student();
        student.setEmail(email);
        student.setName(username); // Using username as name initially
        studentRepo.save(student);

        return userRepo.save(user);
    }

    public String login(String username, String password) {
        Users user = userRepo.findByUsername(username);

        if (user == null) throw new RuntimeException("User not found");

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtService.generateToken(username, user.getRole().name());
    }

}
