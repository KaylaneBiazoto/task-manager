package com.example.task_manager_backend.features.tasks.usecases;

import com.example.task_manager_backend.features.tasks.core.TaskNotFoundException;
import com.example.task_manager_backend.features.tasks.domain.Task;
import com.example.task_manager_backend.features.tasks.repositories.TaskRepository;
import org.springframework.stereotype.Component;

@Component
public class GetTaskByIdHandler {
    private final TaskRepository taskRepository;

    public GetTaskByIdHandler(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task execute(Long taskId) {
        return taskRepository.findByIdAndActiveTrue(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
    }
}

