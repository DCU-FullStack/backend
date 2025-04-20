package com.mrhyun.backend.service;

import com.mrhyun.backend.model.Camera;
import com.mrhyun.backend.repository.CameraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CameraService {
    private final CameraRepository cameraRepository;

    public CameraService(CameraRepository cameraRepository) {
        this.cameraRepository = cameraRepository;
    }

    public List<Camera> getAllCameras() {
        return cameraRepository.findAll();
    }

    public Camera getCameraById(Long id) {
        return cameraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("카메라를 찾을 수 없습니다."));
    }

    public Camera createCamera(Camera camera) {
        return cameraRepository.save(camera);
    }

    public Camera updateCamera(Long id, Camera camera) {
        Camera existingCamera = getCameraById(id);
        existingCamera.setName(camera.getName());
        existingCamera.setLocation(camera.getLocation());
        existingCamera.setStatus(camera.getStatus());
        existingCamera.setImageUrl(camera.getImageUrl());
        return cameraRepository.save(existingCamera);
    }

    public void deleteCamera(Long id) {
        cameraRepository.deleteById(id);
    }

    public List<Camera> getCamerasByStatus(String status) {
        return cameraRepository.findByStatus(status);
    }

    public List<Camera> searchCamerasByLocation(String location) {
        return cameraRepository.findByLocationContaining(location);
    }
} 