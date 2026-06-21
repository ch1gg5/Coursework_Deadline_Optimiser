package com.chiggs.coursework_deadline_optimiser.service;

import com.chiggs.coursework_deadline_optimiser.enums.Role;
import com.chiggs.coursework_deadline_optimiser.model.Users;
import com.chiggs.coursework_deadline_optimiser.repo.UserRepo;
import com.chiggs.coursework_deadline_optimiser.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public Users register(String username, String password) {
        if (userRepo.findByUsername(username) != null) {
            throw new RuntimeException("Username already taken");
        }

        Users user = new Users();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // BCrypt hash
        user.setRole(Role.ROLE_USER); // default role
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
