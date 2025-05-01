package com.chill.backend.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String name;
    private String email;
    private String phoneNumber;
    private String phone_number; // For backward compatibility
} 