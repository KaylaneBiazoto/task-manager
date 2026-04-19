package com.example.task_manager_backend.features.projects.services;

import com.example.task_manager_backend.features.projects.core.dto.AddProjectMemberRequest;
import com.example.task_manager_backend.features.projects.core.dto.CreateProjectRequest;
import com.example.task_manager_backend.features.projects.core.dto.ProjectDto;
import com.example.task_manager_backend.features.projects.core.dto.UpdateProjectRequest;
import com.example.task_manager_backend.features.projects.domain.Project;
import com.example.task_manager_backend.features.projects.domain.ProjectMember;
import com.example.task_manager_backend.features.projects.usecases.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final CreateProjectHandler createProjectHandler;
    private final GetProjectByIdHandler getProjectByIdHandler;
    private final ListProjectsHandler listProjectsHandler;
    private final UpdateProjectHandler updateProjectHandler;
    private final DeleteProjectHandler deleteProjectHandler;
    private final AddProjectMemberHandler addProjectMemberHandler;
    private final GetProjectMembersHandler getProjectMembersHandler;

    public ProjectService(
            CreateProjectHandler createProjectHandler,
            GetProjectByIdHandler getProjectByIdHandler,
            ListProjectsHandler listProjectsHandler,
            UpdateProjectHandler updateProjectHandler,
            DeleteProjectHandler deleteProjectHandler,
            AddProjectMemberHandler addProjectMemberHandler,
            GetProjectMembersHandler getProjectMembersHandler) {
        this.createProjectHandler = createProjectHandler;
        this.getProjectByIdHandler = getProjectByIdHandler;
        this.listProjectsHandler = listProjectsHandler;
        this.updateProjectHandler = updateProjectHandler;
        this.deleteProjectHandler = deleteProjectHandler;
        this.addProjectMemberHandler = addProjectMemberHandler;
        this.getProjectMembersHandler = getProjectMembersHandler;
    }

    public ProjectDto createProject(CreateProjectRequest request, UUID ownerId) {
        Project project = createProjectHandler.execute(request, ownerId);
        return mapToDto(project);
    }

    public ProjectDto getProjectById(UUID projectId) {
        Project project = getProjectByIdHandler.execute(projectId);
        return mapToDto(project);
    }

    public Page<ProjectDto> listProjects(Pageable pageable) {
        Page<Project> projects = listProjectsHandler.execute(pageable);
        return projects.map(this::mapToDto);
    }
    
    public Page<ProjectDto> listProjects(String search, UUID ownerId, Pageable pageable) {
        Page<Project> projects = listProjectsHandler.execute(search, ownerId, pageable);
        return projects.map(this::mapToDto);
    }

    public ProjectDto updateProject(UUID projectId, UpdateProjectRequest request) {
        Project project = updateProjectHandler.execute(projectId, request);
        return mapToDto(project);
    }

    public void deleteProject(UUID projectId) {
        deleteProjectHandler.execute(projectId);
    }

    public ProjectDto.ProjectMemberDto addMember(UUID projectId, AddProjectMemberRequest request) {
        ProjectMember member = addProjectMemberHandler.execute(projectId, request);
        return mapMemberToDto(member);
    }

    public List<ProjectDto.ProjectMemberDto> getMembers(UUID projectId) {
        List<ProjectMember> members = getProjectMembersHandler.execute(projectId);
        return members.stream()
                .map(this::mapMemberToDto)
                .collect(Collectors.toList());
    }

    private ProjectDto mapToDto(Project project) {
        List<ProjectDto.ProjectMemberDto> members = null;
        String ownerName = project.getOwnerId() != null ? "" : null;

        if (project.getMembers() != null && !project.getMembers().isEmpty()) {
            // Encontrar o owner (ADMIN)
            ProjectMember ownerMember = project.getMembers().stream()
                    .filter(m -> m.getActive() && "ADMIN".equals(m.getProjectRole()))
                    .findFirst()
                    .orElse(null);

            if (ownerMember != null) {
                ownerName = ownerMember.getUser().getUsername();
            }

            // Mapear todos os membros
            members = project.getMembers().stream()
                    .filter(ProjectMember::getActive)
                    .map(this::mapMemberToDto)
                    .collect(Collectors.toList());
        }

        return new ProjectDto(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getOwnerId(),
                ownerName,
                members,
                project.getActive(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }

    private ProjectDto.ProjectMemberDto mapMemberToDto(ProjectMember member) {
        return new ProjectDto.ProjectMemberDto(
                member.getId(),
                member.getUser().getId(),
                member.getUser().getUsername(),
                member.getProjectRole(),
                member.getActive()
        );
    }
}



