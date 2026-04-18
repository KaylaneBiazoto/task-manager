package com.example.task_manager_backend.features.tasks.core;

import com.example.task_manager_backend.features.tasks.domain.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Priority is required")
    private TaskPriority priority;

    private LocalDate deadline;

    @NotNull(message = "Project ID is required")
    private Long projectId;

    @NotNull(message = "Assignee ID is required")
    private Long assigneeId;
}

