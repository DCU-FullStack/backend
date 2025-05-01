package com.chill.backend.service;

import com.chill.backend.model.Task;
import com.chill.backend.model.TaskStatus;
import com.chill.backend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    @Transactional
    public Task createTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }

        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            task.setTitle("Untitled Task");
        }

        if (task.getLocation() == null || task.getLocation().trim().isEmpty()) {
            task.setLocation("Unknown Location");
        }

        if (task.getDetectionType() == null || task.getDetectionType().trim().isEmpty()) {
            task.setDetectionType("Unknown Type");
        }

        if (task.getTimestamp() == null) {
            task.setTimestamp(LocalDateTime.now());
        }

        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.IN_PROGRESS);
        }

        return taskRepository.save(task);
    }

    @Transactional
    public Task updateTask(Long id, Task task) {
        Task existingTask = getTaskById(id);
        if (existingTask != null) {
            task.setId(id);
            return taskRepository.save(task);
        }
        return null;
    }

    @Transactional
    public Task updateTaskStatus(Long id, TaskStatus status) {
        Task task = getTaskById(id);
        if (task != null) {
            task.setStatus(status);
            return taskRepository.save(task);
        }
        return null;
    }

    @Transactional
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    public List<Task> searchTasksByLocation(String location) {
        return taskRepository.findByLocationContaining(location);
    }
} 