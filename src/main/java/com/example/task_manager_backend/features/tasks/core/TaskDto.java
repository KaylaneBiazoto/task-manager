package com.example.task_manager_backend.features.tasks.core;

import com.example.task_manager_backend.features.tasks.domain.TaskPriority;
import com.example.task_manager_backend.features.tasks.domain.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDate deadline;
    private Long projectId;
    private Long assigneeId;
    private String assigneeName;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}

