package com.company.project.service;

import com.company.project.model.Task;
import com.company.project.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.*;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public int createTask(Map<String, Object> payload) {
        Task task = new Task();
        task.setTaskId(UUID.randomUUID());
        task.setTitle((String) payload.get("title"));
        task.setDescription((String) payload.get("description"));

        Object dueDateObj = payload.get("due_date");
        if (dueDateObj != null)
            task.setDueDate(Timestamp.valueOf(dueDateObj.toString()));

        task.setPriority((String) payload.getOrDefault("priority", "MEDIUM"));

        // 👉 Respect provided status if available, else default to TODO
        String status = (String) payload.get("status");
        if (status == null || status.isEmpty()) {
            status = "TODO";
        }
        task.setStatus(status);

        task.setUserId(UUID.fromString((String) payload.get("user_id")));
        task.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        task.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        return taskRepository.createTask(task);
    }

    public List<Task> getTasks(UUID userId) {
        return taskRepository.getTasksByUserId(userId);
    }

    public List<Task> getAllTasks() {
        return taskRepository.getAllTasks();
    }

//    public int updateTask(UUID taskId, Map<String, Object> payload) {
//        Task task = new Task();
//        task.setTitle((String) payload.get("title"));
//        task.setDescription((String) payload.get("description"));
//        if (payload.get("due_date") != null)
//            task.setDueDate(Timestamp.valueOf(payload.get("due_date").toString()));
//        task.setPriority((String) payload.get("priority"));
//        task.setStatus((String) payload.get("status"));
//        return taskRepository.updateTask(taskId, task);
//    }

    public int updateTask(UUID taskId, Map<String, Object> payload) {
        return taskRepository.updateTaskFields(taskId, payload);
    }

    public int deleteTask(UUID taskId) {
        return taskRepository.deleteTask(taskId);
    }

    public void autoMarkOverdue() {
        List<Task> overdueTasks = taskRepository.getOverdueTasks();
        for (Task task : overdueTasks) {
            taskRepository.markTaskOverdue(task.getTaskId());
        }
    }

}
