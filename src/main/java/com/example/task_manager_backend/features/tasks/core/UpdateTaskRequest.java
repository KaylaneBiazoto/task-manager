package com.example.task_manager_backend.features.tasks.core;

import com.example.task_manager_backend.features.tasks.domain.TaskPriority;
import com.example.task_manager_backend.features.tasks.domain.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskRequest {
    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private LocalDate deadline;

    private Long assigneeId;
}

