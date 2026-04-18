package com.example.task_manager_backend.features.projects.usecases;

import com.example.task_manager_backend.features.projects.domain.Project;
import com.example.task_manager_backend.features.projects.repositories.ProjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class ListProjectsHandler {
    private final ProjectRepository projectRepository;

    public ListProjectsHandler(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Page<Project> execute(Pageable pageable) {
        return projectRepository.findByActiveTrue(pageable);
    }
}

