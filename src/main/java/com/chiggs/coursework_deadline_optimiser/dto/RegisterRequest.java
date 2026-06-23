package com.chiggs.coursework_deadline_optimiser.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;

    // getters and setters
}
