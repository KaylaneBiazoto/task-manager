package com.example.task_manager_backend.features.projects.api;

import com.example.task_manager_backend.core.dto.ApiResponse;
import com.example.task_manager_backend.features.projects.core.*;
import com.example.task_manager_backend.features.projects.services.ProjectService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProjectDto>> createProject(
            @Valid @RequestBody CreateProjectRequest request) {
        ProjectDto project = projectService.createProject(request, request.ownerId());
        ApiResponse<ProjectDto> response = new ApiResponse<>(true, "Project created successfully", project);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ApiResponse<ProjectDto>> getProject(@PathVariable Long projectId) {
        ProjectDto project = projectService.getProjectById(projectId);
        ApiResponse<ProjectDto> response = new ApiResponse<>(true, "Project retrieved successfully", project);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProjectDto>>> listProjects(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long ownerId,
            Pageable pageable) {
        Page<ProjectDto> projects = projectService.listProjects(search, ownerId, pageable);
        ApiResponse<Page<ProjectDto>> response = new ApiResponse<>(true, "Projects retrieved successfully", projects);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ApiResponse<ProjectDto>> updateProject(
            @PathVariable Long projectId,
            @Valid @RequestBody UpdateProjectRequest request) {
        ProjectDto project = projectService.updateProject(projectId, request);
        ApiResponse<ProjectDto> response = new ApiResponse<>(true, "Project updated successfully", project);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<ApiResponse<Void>> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        ApiResponse<Void> response = new ApiResponse<>(true, "Project deleted successfully", null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{projectId}/members")
    public ResponseEntity<ApiResponse<ProjectDto.ProjectMemberDto>> addMember(
            @PathVariable Long projectId,
            @Valid @RequestBody AddProjectMemberRequest request) {
        ProjectDto.ProjectMemberDto member = projectService.addMember(projectId, request);
        ApiResponse<ProjectDto.ProjectMemberDto> response = new ApiResponse<>(true, "Member added successfully", member);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{projectId}/members")
    public ResponseEntity<ApiResponse<List<ProjectDto.ProjectMemberDto>>> getMembers(@PathVariable Long projectId) {
        List<ProjectDto.ProjectMemberDto> members = projectService.getMembers(projectId);
        ApiResponse<List<ProjectDto.ProjectMemberDto>> response = new ApiResponse<>(true, "Members retrieved successfully", members);
        return ResponseEntity.ok(response);
    }
}



