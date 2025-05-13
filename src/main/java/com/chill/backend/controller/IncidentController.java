package com.chill.backend.controller;

import com.chill.backend.model.Incident;
import com.chill.backend.service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
public class IncidentController {
    private final IncidentService incidentService;

    @GetMapping
    public ResponseEntity<List<Incident>> getAllIncidents() {
        return ResponseEntity.ok(incidentService.getAllIncidents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Incident> getIncidentById(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.getIncidentById(id));
    }

    @PostMapping
    public ResponseEntity<Incident> createIncident(@RequestBody Incident incident) {
        try {
            // 필수 필드 검증
            if (incident.getTitle() == null || incident.getTitle().trim().isEmpty()) {
                incident.setTitle("포트홀 감지"); // 기본값 설정
            }
            if (incident.getDetectionType() == null || incident.getDetectionType().trim().isEmpty()) {
                incident.setDetectionType("Pothole"); // 기본값 설정
            }
            if (incident.getLocation() == null || incident.getLocation().trim().isEmpty()) {
                incident.setLocation("위치 정보 없음"); // 기본값 설정
            }
            if (incident.getTimestamp() == null) {
                incident.setTimestamp(LocalDateTime.now()); // 현재 시간으로 설정
            }
            
            return ResponseEntity.ok(incidentService.createIncident(incident));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Incident> updateIncident(@PathVariable Long id, @RequestBody Incident incident) {
        return ResponseEntity.ok(incidentService.updateIncident(id, incident));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncident(@PathVariable Long id) {
        try {
            incidentService.deleteIncident(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Incident>> searchIncidentsByLocation(@RequestParam String location) {
        return ResponseEntity.ok(incidentService.searchIncidentsByLocation(location));
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getIncidentStatistics() {
        return ResponseEntity.ok(incidentService.getIncidentStatistics());
    }
} 