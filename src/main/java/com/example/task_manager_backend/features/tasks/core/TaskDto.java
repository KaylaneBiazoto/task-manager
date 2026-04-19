package com.example.task_manager_backend.features.tasks.core;

import com.example.task_manager_backend.features.tasks.domain.TaskPriority;
import com.example.task_manager_backend.features.tasks.domain.TaskStatus;

import java.time.Instant;
import java.time.LocalDate;

public record TaskDto(
    Long id,
    String title,
    String description,
    TaskStatus status,
    TaskPriority priority,
    LocalDate deadline,
    Long projectId,
    Long assigneeId,
    String assigneeName,
    Instant createdAt,
    Instant updatedAt
) {
}

