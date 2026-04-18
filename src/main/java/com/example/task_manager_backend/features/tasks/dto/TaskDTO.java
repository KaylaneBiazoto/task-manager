package com.example.task_manager_backend.features.tasks.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Simple DTO for tasks.
 */
public class TaskDTO {
    public UUID id;
    public UUID projectId;
    public UUID assigneeId;
    public String title;
    public String description;
    public String status;
    public String priority;
    public LocalDate deadline;
    public Instant createdAt;
    public Instant updatedAt;
}

