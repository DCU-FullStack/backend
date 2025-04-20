package com.mrhyun.backend.repository;

import com.mrhyun.backend.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(String status);
    List<Task> findByAssignedTo_Id(Long userId);
    List<Task> findByLocationContaining(String location);
    List<Task> findByDueDateBeforeAndStatusNot(java.time.LocalDateTime date, String status);
} 