package com.chill.backend.repository;

import com.chill.backend.model.Camera;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CameraRepository extends JpaRepository<Camera, Long> {
    List<Camera> findByStatus(String status);
    List<Camera> findByLocationContaining(String location);
} 