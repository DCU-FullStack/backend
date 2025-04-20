package com.mrhyun.backend.controller;

import com.mrhyun.backend.model.Incident;
import com.mrhyun.backend.service.IncidentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {
    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @GetMapping
    public ResponseEntity<List<Incident>> getAllIncidents() {
        return ResponseEntity.ok(incidentService.getAllIncidents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Incident> getIncidentById(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.getIncidentById(id));
    }

    @PostMapping
    public ResponseEntity<Incident> createIncident(@RequestBody Incident incident, Authentication authentication) {
        return ResponseEntity.ok(incidentService.createIncident(incident, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Incident> updateIncident(@PathVariable Long id, @RequestBody Incident incident) {
        return ResponseEntity.ok(incidentService.updateIncident(id, incident));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncident(@PathVariable Long id) {
        incidentService.deleteIncident(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Incident>> getIncidentsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(incidentService.getIncidentsByStatus(status));
    }

    @GetMapping("/severity/{severity}")
    public ResponseEntity<List<Incident>> getIncidentsBySeverity(@PathVariable String severity) {
        return ResponseEntity.ok(incidentService.getIncidentsBySeverity(severity));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Incident>> searchIncidentsByLocation(@RequestParam String location) {
        return ResponseEntity.ok(incidentService.searchIncidentsByLocation(location));
    }

    @GetMapping("/my-incidents")
    public ResponseEntity<List<Incident>> getMyIncidents(Authentication authentication) {
        return ResponseEntity.ok(incidentService.getIncidentsByUser(authentication.getName()));
    }
} 