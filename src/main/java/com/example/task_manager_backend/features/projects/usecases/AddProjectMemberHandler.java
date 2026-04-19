package com.example.task_manager_backend.features.projects.usecases;
import java.util.UUID;

import com.example.task_manager_backend.features.auth.domain.User;
import com.example.task_manager_backend.features.auth.repositories.UserRepository;
import com.example.task_manager_backend.features.projects.core.dto.AddProjectMemberRequest;
import com.example.task_manager_backend.features.projects.core.exception.ProjectBusinessException;
import com.example.task_manager_backend.features.projects.core.exception.ProjectNotFoundException;
import com.example.task_manager_backend.features.projects.domain.Project;
import com.example.task_manager_backend.features.projects.domain.ProjectMember;
import com.example.task_manager_backend.features.projects.repositories.ProjectRepository;
import com.example.task_manager_backend.features.projects.repositories.ProjectMemberRepository;
import org.springframework.stereotype.Component;

@Component
public class AddProjectMemberHandler {
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;

    public AddProjectMemberHandler(
            ProjectRepository projectRepository,
            ProjectMemberRepository projectMemberRepository,
            UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.userRepository = userRepository;
    }

    public ProjectMember execute(UUID projectId, AddProjectMemberRequest request) {
        // Validar projeto
        Project project = projectRepository.findByIdAndActiveTrue(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        // Validar usuário (apenas ativos)
        User user = userRepository.findByIdAndActiveTrue(request.userId())
                .orElseThrow(() -> new ProjectBusinessException("User not found"));

        // Verificar se já é membro
        boolean isAlreadyMember = project.getMembers().stream()
                .anyMatch(member -> member.getUser().getId().equals(user.getId()) && member.getActive());

        if (isAlreadyMember) {
            throw new ProjectBusinessException("User is already a member of this project");
        }

        // Adicionar membro
        ProjectMember member = new ProjectMember();
        member.setProject(project);
        member.setUser(user);
        member.setProjectRole(request.projectRole());
        member.setActive(true);

        return projectMemberRepository.save(member);
    }
}

