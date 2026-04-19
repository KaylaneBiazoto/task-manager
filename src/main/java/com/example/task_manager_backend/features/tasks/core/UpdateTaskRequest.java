package com.example.task_manager_backend.features.tasks.core;

import com.example.task_manager_backend.features.tasks.domain.TaskPriority;
import com.example.task_manager_backend.features.tasks.domain.TaskStatus;

import java.time.LocalDate;

public record UpdateTaskRequest(
    String title,

    String description,

    TaskStatus status,

    TaskPriority priority,

    LocalDate deadline,

    Long assigneeId
) {
}

