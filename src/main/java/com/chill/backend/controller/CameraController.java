package com.chill.backend.controller;

import com.chill.backend.model.Camera;
import com.chill.backend.service.CameraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cameras")
public class CameraController {
    private final CameraService cameraService;

    public CameraController(CameraService cameraService) {
        this.cameraService = cameraService;
    }

    @GetMapping
    public ResponseEntity<List<Camera>> getAllCameras() {
        return ResponseEntity.ok(cameraService.getAllCameras());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Camera> getCameraById(@PathVariable Long id) {
        return ResponseEntity.ok(cameraService.getCameraById(id));
    }

    @PostMapping
    public ResponseEntity<Camera> createCamera(@RequestBody Camera camera) {
        return ResponseEntity.ok(cameraService.createCamera(camera));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Camera> updateCamera(@PathVariable Long id, @RequestBody Camera camera) {
        return ResponseEntity.ok(cameraService.updateCamera(id, camera));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCamera(@PathVariable Long id) {
        cameraService.deleteCamera(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Camera>> getCamerasByStatus(@PathVariable String status) {
        return ResponseEntity.ok(cameraService.getCamerasByStatus(status));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Camera>> searchCamerasByLocation(@RequestParam String location) {
        return ResponseEntity.ok(cameraService.searchCamerasByLocation(location));
    }
} 