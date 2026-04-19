package com.example.task_manager_backend.features.projects.usecases;
import java.util.UUID;

import com.example.task_manager_backend.features.projects.core.exception.ProjectNotFoundException;
import com.example.task_manager_backend.features.projects.domain.Project;
import com.example.task_manager_backend.features.projects.repositories.ProjectRepository;
import org.springframework.stereotype.Component;

@Component
public class GetProjectByIdHandler {
    private final ProjectRepository projectRepository;

    public GetProjectByIdHandler(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project execute(UUID projectId) {
        return projectRepository.findByIdAndActiveTrue(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));
    }
}

