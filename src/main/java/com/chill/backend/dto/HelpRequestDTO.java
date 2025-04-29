package com.chill.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HelpRequestDTO {
    private String name;
    private String email;
    private String subject;
    private String message;
    private LocalDateTime createdAt = LocalDateTime.now();;
    private String status;

    public HelpRequestDTO(String name, String email, String subject, String message, LocalDateTime createdAt, String status) {
        this.name = name;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.createdAt = createdAt;
        this.status = status;
    }
}
