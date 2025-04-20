package com.mrhyun.backend.service;

import com.mrhyun.backend.model.Task;
import com.mrhyun.backend.model.User;
import com.mrhyun.backend.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("작업을 찾을 수 없습니다."));
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task task) {
        Task existingTask = getTaskById(id);
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setLocation(task.getLocation());
        existingTask.setStatus(task.getStatus());
        existingTask.setDueDate(task.getDueDate());
        if (task.getAssignedTo() != null) {
            User assignedUser = userService.findByUsername(task.getAssignedTo().getUsername());
            existingTask.setAssignedTo(assignedUser);
        }
        return taskRepository.save(existingTask);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public List<Task> getTasksByStatus(String status) {
        return taskRepository.findByStatus(status);
    }

    public List<Task> getTasksByUser(String username) {
        User user = userService.findByUsername(username);
        return taskRepository.findByAssignedTo_Id(user.getId());
    }

    public List<Task> searchTasksByLocation(String location) {
        return taskRepository.findByLocationContaining(location);
    }

    public List<Task> getOverdueTasks() {
        return taskRepository.findByDueDateBeforeAndStatusNot(LocalDateTime.now(), "completed");
    }
} 