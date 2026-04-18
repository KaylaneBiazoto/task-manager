package com.example.task_manager_backend.features.tasks.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Domain placeholder for Task entity.
 */
public class Task {
    private UUID id;
    private UUID projectId;
    private UUID assigneeId;
    private String title;
    private String description;
    private String status;
    private String priority;
    private LocalDate deadline;
    private Instant createdAt;
    private Instant updatedAt;

    // getters/setters omitted
}

