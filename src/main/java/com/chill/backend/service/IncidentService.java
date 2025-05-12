package com.chill.backend.service;

import com.chill.backend.model.Incident;
import com.chill.backend.model.Camera;
import com.chill.backend.repository.IncidentRepository;
import com.chill.backend.repository.CameraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class IncidentService {
    private final IncidentRepository incidentRepository;
    private final CameraRepository cameraRepository;

    public List<Incident> getAllIncidents() {
        return incidentRepository.findAllByOrderByIdAsc();
    }

    public Incident getIncidentById(Long id) {
        return incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found with id: " + id));
    }

    @Transactional
    public Incident createIncident(Incident incident) {
        if (incident.getCamera() != null) {
            Camera camera = cameraRepository.findById(incident.getCamera().getId())
                    .orElseGet(() -> {
                        Camera newCamera = new Camera();
                        newCamera.setId(incident.getCamera().getId());
                        newCamera.setName(incident.getCamera().getName());
                        newCamera.setLocation(incident.getCamera().getLocation());
                        return cameraRepository.save(newCamera);
                    });
            incident.setCamera(camera);
        }
        
        incident.setTimestamp(LocalDateTime.now());
        return incidentRepository.save(incident);
    }

    @Transactional
    public Incident updateIncident(Long id, Incident incident) {
        Incident existingIncident = getIncidentById(id);
        existingIncident.setDetectionType(incident.getDetectionType());
        existingIncident.setConfidence(incident.getConfidence());
        existingIncident.setLocation(incident.getLocation());
        return incidentRepository.save(existingIncident);
    }

    @Transactional
    public void deleteIncident(Long id) {
        try {
            log.info("Attempting to delete incident with ID: {}", id);
            
            if (id == null) {
                log.error("Attempted to delete incident with null ID");
                throw new IllegalArgumentException("Incident ID cannot be null");
            }

            if (!incidentRepository.existsById(id)) {
                log.warn("Incident with ID {} not found for deletion", id);
                return; // 존재하지 않는 경우 예외를 발생시키지 않고 조용히 반환
            }

            incidentRepository.deleteById(id);
            log.info("Successfully deleted incident with ID: {}", id);
        } catch (Exception e) {
            log.error("Error deleting incident with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to delete incident: " + e.getMessage(), e);
        }
    }

    public List<Incident> searchIncidentsByLocation(String location) {
        return incidentRepository.findByLocationContaining(location);
    }

    public void updateAllIncidents(List<Incident> incidents) {
        incidentRepository.saveAll(incidents);
    }

    public Map<String, Object> getIncidentStatistics() {
        List<Incident> allIncidents = incidentRepository.findAllByOrderByIdAsc();
        long totalIncidents = allIncidents.size();
        long lastIncidentId = allIncidents.isEmpty() ? 0 : allIncidents.get(allIncidents.size() - 1).getId();
        long resolvedIncidents = lastIncidentId - totalIncidents;
        long inProgressIncidents = totalIncidents;

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("total", lastIncidentId);
        statistics.put("resolved", resolvedIncidents);
        statistics.put("inProgress", inProgressIncidents);

        return statistics;
    }
} 