package com.example.task_manager_backend.features.tasks.usecases;

import com.example.task_manager_backend.features.tasks.core.exception.TaskBusinessException;
import com.example.task_manager_backend.features.tasks.core.exception.TaskNotFoundException;
import com.example.task_manager_backend.features.tasks.repositories.TaskRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DeleteTaskHandler {
    private final TaskRepository taskRepository;

    public DeleteTaskHandler(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void execute(UUID taskId) {
        var task = taskRepository.findByIdAndActiveTrue(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        // Regra: Se a tarefa tem um assignee, não pode ser deletada
        if (task.getAssignee() != null) {
            throw new TaskBusinessException("Cannot delete a task that has an assignee. Please remove the assignee first.");
        }

        task.setActive(false);
        taskRepository.save(task);
    }
}

