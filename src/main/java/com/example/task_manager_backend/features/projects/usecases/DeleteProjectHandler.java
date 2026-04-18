package com.example.task_manager_backend.features.projects.usecases;

import com.example.task_manager_backend.features.projects.core.ProjectNotFoundException;
import com.example.task_manager_backend.features.projects.domain.Project;
import com.example.task_manager_backend.features.projects.repositories.ProjectRepository;
import org.springframework.stereotype.Component;

@Component
public class DeleteProjectHandler {
    private final ProjectRepository projectRepository;

    public DeleteProjectHandler(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public void execute(Long projectId) {
        Project project = projectRepository.findByIdAndActiveTrue(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        project.setActive(false);
        projectRepository.save(project);
    }
}

