package com.company.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private UUID taskId;
    private String title;
    private String description;
    private Timestamp dueDate;
    private String priority;   // LOW, MEDIUM, HIGH
    private String status;     // TODO, IN_PROGRESS, DONE, OVERDUE
    private UUID userId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}

