package com.chill.backend.repository;

import com.chill.backend.model.Task;
import com.chill.backend.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByOrderByIdDesc();
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByLocationContaining(String location);
} 