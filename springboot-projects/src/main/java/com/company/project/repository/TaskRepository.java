package com.company.project.repository;

import com.company.project.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class TaskRepository {
    private final RowMapper<Task> taskRowMapper = new RowMapper<Task>() {
        @Override
        public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
            Task task = new Task();
            task.setTaskId(UUID.fromString(rs.getString("task_id")));        // ✅ changed
            task.setTitle(rs.getString("title"));
            task.setDescription(rs.getString("description"));
            task.setDueDate(rs.getTimestamp("due_date"));
            task.setPriority(rs.getString("priority"));
            task.setStatus(rs.getString("status"));
            task.setUserId(UUID.fromString(rs.getString("user_id")));        // ✅ changed
            task.setCreatedAt(rs.getTimestamp("created_at"));
            task.setUpdatedAt(rs.getTimestamp("updated_at"));
            return task;
        }
    };

    @Autowired
    private JdbcTemplate jdbcTemplate;
    public int createTask(Task task) {
        String sql = "INSERT INTO tasks (task_id, title, description, due_date, priority, status, user_id, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                task.getTaskId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getPriority(),
                task.getStatus(),
                task.getUserId(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }

    public List<Task> getTasksByUserId(UUID userId) {
        String sql = "SELECT * FROM tasks WHERE user_id = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, taskRowMapper);
    }

    public List<Task> getAllTasks() {
        String sql = "SELECT * FROM tasks";
        return jdbcTemplate.query(sql, taskRowMapper);
    }

    public int updateTask(UUID taskId, Task task) {
        String sql = "UPDATE tasks SET title = ?, description = ?, due_date = ?, priority = ?, status = ?, updated_at = ? WHERE task_id = ?";
        return jdbcTemplate.update(sql,
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getPriority(),
                task.getStatus(),
                new Timestamp(System.currentTimeMillis()),
                taskId
        );
    }

    public int updateTaskFields(UUID taskId, Map<String, Object> fields) {
        StringBuilder sql = new StringBuilder("UPDATE tasks SET ");
        List<Object> params = new ArrayList<>();

        if (fields.containsKey("title")) {
            sql.append("title = ?, ");
            params.add(fields.get("title"));
        }
        if (fields.containsKey("description")) {
            sql.append("description = ?, ");
            params.add(fields.get("description"));
        }
        if (fields.containsKey("due_date")) {
            sql.append("due_date = ?, ");
            params.add(Timestamp.valueOf(fields.get("due_date").toString()));
        }
        if (fields.containsKey("priority")) {
            sql.append("priority = ?, ");
            params.add(fields.get("priority"));
        }
        if (fields.containsKey("status")) {
            sql.append("status = ?, ");
            params.add(fields.get("status"));
        }

        // Always update timestamp
        sql.append("updated_at = CURRENT_TIMESTAMP WHERE task_id = ?");
        params.add(taskId);

        return jdbcTemplate.update(sql.toString(), params.toArray());
    }

    public int deleteTask(UUID taskId) {
        String sql = "DELETE FROM tasks WHERE task_id = ?";
        return jdbcTemplate.update(sql, taskId);
    }

    public List<Task> getOverdueTasks() {
        String sql = "SELECT * FROM tasks WHERE due_date < CURRENT_TIMESTAMP AND status != 'DONE'";
        return jdbcTemplate.query(sql, taskRowMapper);
    }

    public int markTaskOverdue(UUID taskId) {
        String sql = "UPDATE tasks SET status = 'OVERDUE', updated_at = CURRENT_TIMESTAMP WHERE task_id = ?";
        return jdbcTemplate.update(sql, taskId);
    }
}

