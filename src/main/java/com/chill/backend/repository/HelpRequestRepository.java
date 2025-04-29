package com.chill.backend.repository;

import com.chill.backend.model.HelpRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HelpRequestRepository extends JpaRepository<HelpRequestEntity, Long> {
    List<HelpRequestEntity> findAllByOrderByCreatedAtDesc();
}