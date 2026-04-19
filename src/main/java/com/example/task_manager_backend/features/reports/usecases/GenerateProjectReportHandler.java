package com.example.task_manager_backend.features.reports.usecases;

import com.example.task_manager_backend.features.projects.repositories.ProjectRepository;
import com.example.task_manager_backend.features.reports.core.dto.TaskReportDto;
import com.example.task_manager_backend.features.reports.core.exception.ProjectNotFoundException;
import com.example.task_manager_backend.features.tasks.repositories.TaskRepository;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GenerateProjectReportHandler {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public GenerateProjectReportHandler(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    public TaskReportDto execute(UUID projectId) {
        // Verificar se o projeto existe
        projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Projeto não encontrado: " + projectId));

        Map<String, Long> byStatus = countTasksByStatus(projectId);

        Map<String, Long> byPriority = countTasksByPriority(projectId);

        return new TaskReportDto(projectId, byStatus, byPriority);
    }

    private Map<String, Long> countTasksByStatus(UUID projectId) {
        List<Object[]> results = taskRepository.countTasksByStatus(projectId);
        Map<String, Long> statusCounts = new HashMap<>();

        for (Object[] row : results) {
            String status = row[0].toString();
            Long count = ((Number) row[1]).longValue();
            statusCounts.put(status, count);
        }

        return statusCounts;
    }

    private Map<String, Long> countTasksByPriority(UUID projectId) {
        List<Object[]> results = taskRepository.countTasksByPriority(projectId);
        Map<String, Long> priorityCounts = new HashMap<>();

        for (Object[] row : results) {
            String priority = row[0].toString();
            Long count = ((Number) row[1]).longValue();
            priorityCounts.put(priority, count);
        }

        return priorityCounts;
    }
}


