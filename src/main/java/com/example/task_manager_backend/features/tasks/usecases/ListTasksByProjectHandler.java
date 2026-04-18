package com.example.task_manager_backend.features.tasks.usecases;

import com.example.task_manager_backend.features.tasks.domain.Task;
import com.example.task_manager_backend.features.tasks.repositories.TaskRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListTasksByProjectHandler {
    private final TaskRepository taskRepository;

    public ListTasksByProjectHandler(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> execute(Long projectId) {
        return taskRepository.findByProjectIdAndActiveTrue(projectId);
    }
}

