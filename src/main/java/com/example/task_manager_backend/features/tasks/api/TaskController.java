package com.example.task_manager_backend.features.tasks.api;

import com.example.task_manager_backend.core.dto.ApiResponse;
import com.example.task_manager_backend.features.tasks.core.CreateTaskRequest;
import com.example.task_manager_backend.features.tasks.core.TaskDto;
import com.example.task_manager_backend.features.tasks.core.UpdateTaskRequest;
import com.example.task_manager_backend.features.tasks.services.TaskFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "API for managing tasks")
public class TaskController {
    private final TaskFacade taskFacade;

    public TaskController(TaskFacade taskFacade) {
        this.taskFacade = taskFacade;
    }

    @PostMapping
    @Operation(summary = "Create a new task")
    public ResponseEntity<ApiResponse<TaskDto>> createTask(@Valid @RequestBody CreateTaskRequest request) {
        TaskDto taskDto = taskFacade.createTask(request);
        ApiResponse<TaskDto> response = new ApiResponse<>(true, "Task created successfully", taskDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{taskId}")
    @Operation(summary = "Get task by ID")
    public ResponseEntity<ApiResponse<TaskDto>> getTaskById(@PathVariable Long taskId) {
        TaskDto taskDto = taskFacade.getTaskById(taskId);
        ApiResponse<TaskDto> response = new ApiResponse<>(true, "Task retrieved successfully", taskDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "List tasks by project")
    public ResponseEntity<ApiResponse<Page<TaskDto>>> listTasksByProject(
            @PathVariable Long projectId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) Long assigneeId,
            Pageable pageable) {
        Page<TaskDto> tasks = taskFacade.listTasksByProjectWithFilters(
                projectId, search, status, priority, assigneeId, pageable);
        ApiResponse<Page<TaskDto>> response = new ApiResponse<>(true, "Tasks retrieved successfully", tasks);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{taskId}")
    @Operation(summary = "Update a task")
    public ResponseEntity<ApiResponse<TaskDto>> updateTask(
            @PathVariable Long taskId,
            @Valid @RequestBody UpdateTaskRequest request) {
        // TODO: Extract current user ID and role from security context
        Long currentUserId = 1L;
        String currentUserRole = "ADMIN";

        TaskDto taskDto = taskFacade.updateTask(taskId, request, currentUserId, currentUserRole);
        ApiResponse<TaskDto> response = new ApiResponse<>(true, "Task updated successfully", taskDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{taskId}")
    @Operation(summary = "Delete a task")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable Long taskId) {
        taskFacade.deleteTask(taskId);
        ApiResponse<Void> response = new ApiResponse<>(true, "Task deleted successfully", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}


