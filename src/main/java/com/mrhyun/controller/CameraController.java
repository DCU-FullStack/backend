package com.mrhyun.controller;

import com.mrhyun.entity.Camera;
import com.mrhyun.service.CameraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cameras")
@RequiredArgsConstructor
public class CameraController {
    private final CameraService cameraService;

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
} 