package com.chill.backend.controller;

import com.chill.backend.model.Task;
import com.chill.backend.model.TaskStatus;
import com.chill.backend.model.Incident;
import com.chill.backend.service.TaskService;
import com.chill.backend.service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final IncidentService incidentService;

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        return task != null ? ResponseEntity.ok(task) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return ResponseEntity.ok(taskService.createTask(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        Task updatedTask = taskService.updateTask(id, task);
        return updatedTask != null ? ResponseEntity.ok(updatedTask) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable Long id, @RequestParam TaskStatus status) {
        Task updatedTask = taskService.updateTaskStatus(id, status);
        return updatedTask != null ? ResponseEntity.ok(updatedTask) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable TaskStatus status) {
        return ResponseEntity.ok(taskService.getTasksByStatus(status));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTasksByLocation(@RequestParam String location) {
        return ResponseEntity.ok(taskService.searchTasksByLocation(location));
    }

    @PostMapping("/from-incident")
    @Transactional
    public ResponseEntity<?> createTaskFromIncident(@RequestBody Map<String, Long> body) {
        try {
            Long incidentId = body.get("incidentId");
            if (incidentId == null) {
                return ResponseEntity.badRequest().body("Incident ID is required");
            }

            Incident incident = incidentService.getIncidentById(incidentId);
            if (incident == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Incident not found");
            }

            // task 생성
            Task task = new Task();
            task.setTitle("[대응필요] " + incident.getTitle());
            task.setLocation(incident.getLocation());
            task.setDetectionType(incident.getDetectionType());
            task.setTimestamp(incident.getTimestamp());
            task.setStatus(TaskStatus.IN_PROGRESS);
            task.setCreatedAt(LocalDateTime.now());
            task.setUpdatedAt(LocalDateTime.now());

            Task savedTask = taskService.createTask(task);
            
            // incident 삭제
            incidentService.deleteIncident(incidentId);

            return ResponseEntity.ok(savedTask);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating task: " + e.getMessage());
        }
    }
} 