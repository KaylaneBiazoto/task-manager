package com.example.task_manager_backend.features.tasks.services;

import com.example.task_manager_backend.features.tasks.core.dto.CreateTaskRequest;
import com.example.task_manager_backend.features.tasks.core.dto.TaskDto;
import com.example.task_manager_backend.features.tasks.core.dto.UpdateTaskRequest;
import com.example.task_manager_backend.features.tasks.domain.Task;
import com.example.task_manager_backend.features.tasks.usecases.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskFacade {
    private final CreateTaskHandler createTaskHandler;
    private final GetTaskByIdHandler getTaskByIdHandler;
    private final ListTasksByProjectHandler listTasksByProjectHandler;
    private final ListTasksHandler listTasksHandler;
    private final UpdateTaskHandler updateTaskHandler;
    private final DeleteTaskHandler deleteTaskHandler;

    public TaskFacade(
            CreateTaskHandler createTaskHandler,
            GetTaskByIdHandler getTaskByIdHandler,
            ListTasksByProjectHandler listTasksByProjectHandler,
            ListTasksHandler listTasksHandler,
            UpdateTaskHandler updateTaskHandler,
            DeleteTaskHandler deleteTaskHandler) {
        this.createTaskHandler = createTaskHandler;
        this.getTaskByIdHandler = getTaskByIdHandler;
        this.listTasksByProjectHandler = listTasksByProjectHandler;
        this.listTasksHandler = listTasksHandler;
        this.updateTaskHandler = updateTaskHandler;
        this.deleteTaskHandler = deleteTaskHandler;
    }

    public TaskDto createTask(CreateTaskRequest request) {
        Task task = createTaskHandler.execute(request);
        return mapToDto(task);
    }

    public TaskDto getTaskById(UUID taskId) {
        Task task = getTaskByIdHandler.execute(taskId);
        return mapToDto(task);
    }

    public List<TaskDto> listTasksByProject(UUID projectId) {
        List<Task> tasks = listTasksByProjectHandler.execute(projectId);
        return tasks.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    public Page<TaskDto> listTasksByProjectWithFilters(
            UUID projectId,
            String search,
            String status,
            String priority,
            UUID assigneeId,
            Pageable pageable) {
        Page<Task> tasks = listTasksHandler.executeByProjectWithFilters(
                projectId, search, status, priority, assigneeId, pageable);
        return tasks.map(this::mapToDto);
    }

    public TaskDto updateTask(UUID taskId, UpdateTaskRequest request, UUID currentUserId, String currentUserRole) {
        Task task = updateTaskHandler.execute(taskId, request, currentUserId, currentUserRole);
        return mapToDto(task);
    }

    public void deleteTask(UUID taskId) {
        deleteTaskHandler.execute(taskId);
    }

    private TaskDto mapToDto(Task task) {
        UUID assigneeId = null;
        String assigneeName = null;
        if (task.getAssignee() != null) {
            assigneeId = task.getAssignee().getId();
            assigneeName = task.getAssignee().getUsername();
        }
        
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDeadline(),
                task.getProject().getId(),
                assigneeId,
                assigneeName,
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}



