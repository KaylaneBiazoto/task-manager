package com.example.task_manager_backend.features.tasks.services;

import com.example.task_manager_backend.features.tasks.core.CreateTaskRequest;
import com.example.task_manager_backend.features.tasks.core.TaskDto;
import com.example.task_manager_backend.features.tasks.core.UpdateTaskRequest;
import com.example.task_manager_backend.features.tasks.domain.Task;
import com.example.task_manager_backend.features.tasks.usecases.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskFacade {
    private final CreateTaskHandler createTaskHandler;
    private final GetTaskByIdHandler getTaskByIdHandler;
    private final ListTasksByProjectHandler listTasksByProjectHandler;
    private final UpdateTaskHandler updateTaskHandler;
    private final DeleteTaskHandler deleteTaskHandler;

    public TaskFacade(
            CreateTaskHandler createTaskHandler,
            GetTaskByIdHandler getTaskByIdHandler,
            ListTasksByProjectHandler listTasksByProjectHandler,
            UpdateTaskHandler updateTaskHandler,
            DeleteTaskHandler deleteTaskHandler) {
        this.createTaskHandler = createTaskHandler;
        this.getTaskByIdHandler = getTaskByIdHandler;
        this.listTasksByProjectHandler = listTasksByProjectHandler;
        this.updateTaskHandler = updateTaskHandler;
        this.deleteTaskHandler = deleteTaskHandler;
    }

    public TaskDto createTask(CreateTaskRequest request) {
        Task task = createTaskHandler.execute(request);
        return mapToDto(task);
    }

    public TaskDto getTaskById(Long taskId) {
        Task task = getTaskByIdHandler.execute(taskId);
        return mapToDto(task);
    }

    public List<TaskDto> listTasksByProject(Long projectId) {
        List<Task> tasks = listTasksByProjectHandler.execute(projectId);
        return tasks.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public TaskDto updateTask(Long taskId, UpdateTaskRequest request, Long currentUserId, String currentUserRole) {
        Task task = updateTaskHandler.execute(taskId, request, currentUserId, currentUserRole);
        return mapToDto(task);
    }

    public void deleteTask(Long taskId) {
        deleteTaskHandler.execute(taskId);
    }

    private TaskDto mapToDto(Task task) {
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        dto.setDeadline(task.getDeadline());
        dto.setProjectId(task.getProject().getId());
        if (task.getAssignee() != null) {
            dto.setAssigneeId(task.getAssignee().getId());
            dto.setAssigneeName(task.getAssignee().getUsername());
        }
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());
        return dto;
    }
}

