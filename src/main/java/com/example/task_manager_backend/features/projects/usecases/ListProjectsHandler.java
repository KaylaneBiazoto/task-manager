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
    
    public Page<Project> execute(String search, Long ownerId, Pageable pageable) {
        if ((search == null || search.isEmpty()) && ownerId == null) {
            return projectRepository.findByActiveTrue(pageable);
        }
        
        String finalSearch = (search == null || search.isEmpty()) ? "" : search;
        
        if (ownerId == null) {
            return projectRepository.searchActiveProjects(finalSearch, pageable);
        }
        
        return projectRepository.findActiveWithFilters(finalSearch, ownerId, pageable);
    }
}

