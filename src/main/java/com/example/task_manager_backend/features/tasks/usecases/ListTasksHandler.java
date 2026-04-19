package com.example.task_manager_backend.features.tasks.usecases;
import java.util.UUID;

import com.example.task_manager_backend.features.tasks.domain.Task;
import com.example.task_manager_backend.features.tasks.domain.TaskStatus;
import com.example.task_manager_backend.features.tasks.domain.TaskPriority;
import com.example.task_manager_backend.features.tasks.repositories.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class ListTasksHandler {
    private final TaskRepository taskRepository;

    public ListTasksHandler(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    
    public Page<Task> executeByProjectWithFilters(
            UUID projectId,
            String search,
            String status,
            String priority,
            UUID assigneeId,
            Pageable pageable) {
        
        TaskStatus taskStatus = null;
        if (status != null && !status.isEmpty()) {
            try {
                taskStatus = TaskStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
            }
        }
        
        TaskPriority taskPriority = null;
        if (priority != null && !priority.isEmpty()) {
            try {
                taskPriority = TaskPriority.valueOf(priority.toUpperCase());
            } catch (IllegalArgumentException e) {
            }
        }
        
        String finalSearch = (search == null || search.isEmpty()) ? "" : search;
        
        return taskRepository.findByProjectIdWithFilters(
                projectId,
                finalSearch,
                taskStatus,
                taskPriority,
                assigneeId,
                pageable
        );
    }
}

