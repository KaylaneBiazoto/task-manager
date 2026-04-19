package com.example.task_manager_backend.features.projects.usecases;
import java.util.UUID;

import com.example.task_manager_backend.features.projects.domain.ProjectMember;
import com.example.task_manager_backend.features.projects.repositories.ProjectMemberRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetProjectMembersHandler {
    private final ProjectMemberRepository projectMemberRepository;

    public GetProjectMembersHandler(ProjectMemberRepository projectMemberRepository) {
        this.projectMemberRepository = projectMemberRepository;
    }

    public List<ProjectMember> execute(UUID projectId) {
        return projectMemberRepository.findByProjectIdAndActiveTrue(projectId);
    }
}

