package com.mrhyun.service;

import com.mrhyun.entity.Incident;
import com.mrhyun.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncidentService {
    private final IncidentRepository incidentRepository;

    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    public Incident getIncidentById(Long id) {
        return incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found"));
    }

    public Incident createIncident(Incident incident) {
        return incidentRepository.save(incident);
    }

    public Incident updateIncident(Long id, Incident incident) {
        Incident existingIncident = getIncidentById(id);
        existingIncident.setTitle(incident.getTitle());
        existingIncident.setDescription(incident.getDescription());
        existingIncident.setLocation(incident.getLocation());
        existingIncident.setSeverity(incident.getSeverity());
        existingIncident.setCamera(incident.getCamera());
        existingIncident.setStatus(incident.getStatus());
        return incidentRepository.save(existingIncident);
    }

    public void deleteIncident(Long id) {
        incidentRepository.deleteById(id);
    }
} 