package com.chill.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class HelpRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 기본키, 자동 생성

    private String name;
    private String email;
    private String subject;
    private String message;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;  // 생성 시간 필드
    private String status; // 기본값 "대기중"

    // 기본 생성자
    public HelpRequestEntity() {}

    // 파라미터가 있는 생성자
    public HelpRequestEntity(String name, String email, String subject, String message, LocalDateTime createdAt, String status) {
        this.name = name;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.createdAt = createdAt;
        this.status = status;
    }


    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}
