package com.mrhyun.backend.repository;

import com.mrhyun.backend.model.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByStatus(String status);
    List<Incident> findBySeverity(String severity);
    List<Incident> findByLocationContaining(String location);
    List<Incident> findByReportedBy_Id(Long userId);
} 