package com.chill.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cameras")
public class Camera {
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String status = "온라인";

    @Column(name = "image_url")
    private String imageUrl;
} 