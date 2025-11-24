package com.company.project.controller;

import com.company.project.model.Task;
import com.company.project.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody Map<String, Object> payload) {
        int result = taskService.createTask(payload);
        if (result > 0) {
            return ResponseEntity.ok("Task created successfully!");
        } else {
            return ResponseEntity.badRequest().body("Task creation failed.");
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> getUserTasks(@PathVariable UUID userId) {
        return ResponseEntity.ok(taskService.getTasks(userId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable UUID taskId, @RequestBody Map<String, Object> payload) {
        int result = taskService.updateTask(taskId, payload);
        return result > 0 ? ResponseEntity.ok("Task updated successfully!") : ResponseEntity.badRequest().body("Update failed.");
    }
    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable UUID taskId) {
        int result = taskService.deleteTask(taskId);
        return result > 0 ? ResponseEntity.ok("Task deleted successfully!") : ResponseEntity.badRequest().body("Delete failed.");
    }
}

