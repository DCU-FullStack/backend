package com.chill.backend.service;

import com.chill.backend.model.Incident;
import com.chill.backend.model.User;
import com.chill.backend.repository.IncidentRepository;
import com.chill.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class IncidentService {
    private final IncidentRepository incidentRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    public Incident getIncidentById(Long id) {
        return incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사고를 찾을 수 없습니다."));
    }

    public Incident createIncident(Incident incident, String username) {
        User reporter = userService.findByUsername(username);
        incident.setReportedBy(reporter);
        return incidentRepository.save(incident);
    }

    public Incident updateIncident(Long id, Incident incident) {
        Incident existingIncident = getIncidentById(id);
        existingIncident.setTitle(incident.getTitle());
        existingIncident.setDescription(incident.getDescription());
        existingIncident.setLocation(incident.getLocation());
        existingIncident.setSeverity(incident.getSeverity());
        existingIncident.setStatus(incident.getStatus());
        return incidentRepository.save(existingIncident);
    }

    public void deleteIncident(Long id) {
        incidentRepository.deleteById(id);
    }

    public List<Incident> getIncidentsByStatus(String status) {
        return incidentRepository.findByStatus(status);
    }

    public List<Incident> getIncidentsBySeverity(String severity) {
        return incidentRepository.findBySeverity(severity);
    }

    public List<Incident> searchIncidentsByLocation(String location) {
        return incidentRepository.findByLocationContaining(location);
    }

    public List<Incident> getIncidentsByUser(String username) {
        User user = userService.findByUsername(username);
        return incidentRepository.findByReportedBy_Id(user.getId());
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
} 