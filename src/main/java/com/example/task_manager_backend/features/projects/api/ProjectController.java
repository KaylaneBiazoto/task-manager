package com.example.task_manager_backend.features.projects.api;

import com.example.task_manager_backend.core.dto.ApiResponse;
import com.example.task_manager_backend.features.projects.core.dto.AddProjectMemberRequest;
import com.example.task_manager_backend.features.projects.core.dto.CreateProjectRequest;
import com.example.task_manager_backend.features.projects.core.dto.ProjectDto;
import com.example.task_manager_backend.features.projects.core.dto.UpdateProjectRequest;
import com.example.task_manager_backend.features.projects.services.ProjectService;
import com.example.task_manager_backend.infrastructure.security.SecurityContextService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "API for managing projects")
public class ProjectController {
    private final ProjectService projectService;
    private final SecurityContextService securityContextService;

    public ProjectController(ProjectService projectService, SecurityContextService securityContextService) {
        this.projectService = projectService;
        this.securityContextService = securityContextService;
    }

    @PostMapping
    @Operation(summary = "Create a new project")
    public ResponseEntity<ApiResponse<ProjectDto>> createProject(
            @Valid @RequestBody CreateProjectRequest request) {
        ProjectDto project = projectService.createProject(request, request.ownerId());
        ApiResponse<ProjectDto> response = new ApiResponse<>(true, "Project created successfully", project);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{projectId}")
    @Operation(summary = "Get project by ID")
    public ResponseEntity<ApiResponse<ProjectDto>> getProject(@PathVariable UUID projectId) {
        ProjectDto project = projectService.getProjectById(projectId);
        ApiResponse<ProjectDto> response = new ApiResponse<>(true, "Project retrieved successfully", project);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "List projects with optional filters")
    public ResponseEntity<ApiResponse<Page<ProjectDto>>> listProjects(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) UUID ownerId,
            Pageable pageable) {
        Page<ProjectDto> projects = projectService.listProjects(search, ownerId, pageable);
        ApiResponse<Page<ProjectDto>> response = new ApiResponse<>(true, "Projects retrieved successfully", projects);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{projectId}")
    @Operation(summary = "Update a project")
    public ResponseEntity<ApiResponse<ProjectDto>> updateProject(
            @PathVariable UUID projectId,
            @Valid @RequestBody UpdateProjectRequest request) {
        ProjectDto project = projectService.updateProject(projectId, request);
        ApiResponse<ProjectDto> response = new ApiResponse<>(true, "Project updated successfully", project);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{projectId}")
    @Operation(summary = "Delete a project")
    public ResponseEntity<ApiResponse<Void>> deleteProject(@PathVariable UUID projectId) {
        UUID currentUserId = securityContextService.getCurrentUserId();
        projectService.deleteProject(projectId, currentUserId);
        ApiResponse<Void> response = new ApiResponse<>(true, "Project deleted successfully", null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{projectId}/members")
    @Operation(summary = "Add a member to project")
    public ResponseEntity<ApiResponse<ProjectDto.ProjectMemberDto>> addMember(
            @PathVariable UUID projectId,
            @Valid @RequestBody AddProjectMemberRequest request) {
        ProjectDto.ProjectMemberDto member = projectService.addMember(projectId, request);
        ApiResponse<ProjectDto.ProjectMemberDto> response = new ApiResponse<>(true, "Member added successfully", member);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{projectId}/members")
    @Operation(summary = "Get project members")
    public ResponseEntity<ApiResponse<List<ProjectDto.ProjectMemberDto>>> getMembers(@PathVariable UUID projectId) {
        List<ProjectDto.ProjectMemberDto> members = projectService.getMembers(projectId);
        ApiResponse<List<ProjectDto.ProjectMemberDto>> response = new ApiResponse<>(true, "Members retrieved successfully", members);
        return ResponseEntity.ok(response);
    }
}



