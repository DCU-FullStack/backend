package com.chill.backend.repository;

import com.chill.backend.model.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findAllByOrderByIdAsc();
    List<Incident> findByLocationContaining(String location);
} 