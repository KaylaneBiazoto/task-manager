package com.example.task_manager_backend.features.tasks.usecases;

import com.example.task_manager_backend.features.tasks.core.exception.TaskNotFoundException;
import com.example.task_manager_backend.features.tasks.domain.Task;
import com.example.task_manager_backend.features.tasks.repositories.TaskRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GetTaskByIdHandler {
    private final TaskRepository taskRepository;

    public GetTaskByIdHandler(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task execute(UUID taskId) {
        return taskRepository.findByIdAndActiveTrue(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
    }
}

