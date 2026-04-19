package com.example.task_manager_backend.features.projects.usecases;

import com.example.task_manager_backend.features.projects.core.ProjectNotFoundException;
import com.example.task_manager_backend.features.projects.core.UpdateProjectRequest;
import com.example.task_manager_backend.features.projects.domain.Project;
import com.example.task_manager_backend.features.projects.repositories.ProjectRepository;
import org.springframework.stereotype.Component;

@Component
public class UpdateProjectHandler {
    private final ProjectRepository projectRepository;

    public UpdateProjectHandler(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project execute(Long projectId, UpdateProjectRequest request) {
        Project project = projectRepository.findByIdAndActiveTrue(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        if (request.name() != null && !request.name().isBlank()) {
            project.setName(request.name());
        }

        if (request.description() != null) {
            project.setDescription(request.description());
        }

        return projectRepository.save(project);
    }
}

