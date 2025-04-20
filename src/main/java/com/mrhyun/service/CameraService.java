package com.mrhyun.service;

import com.mrhyun.entity.Camera;
import com.mrhyun.repository.CameraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CameraService {
    private final CameraRepository cameraRepository;

    public List<Camera> getAllCameras() {
        return cameraRepository.findAll();
    }

    public Camera getCameraById(Long id) {
        return cameraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Camera not found"));
    }

    public Camera createCamera(Camera camera) {
        return cameraRepository.save(camera);
    }

    public Camera updateCamera(Long id, Camera camera) {
        Camera existingCamera = getCameraById(id);
        existingCamera.setName(camera.getName());
        existingCamera.setLocation(camera.getLocation());
        existingCamera.setStatus(camera.isStatus());
        return cameraRepository.save(existingCamera);
    }

    public void deleteCamera(Long id) {
        cameraRepository.deleteById(id);
    }
} 