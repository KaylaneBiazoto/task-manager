package com.example.task_manager_backend.features.projects.usecases;

import com.example.task_manager_backend.features.auth.domain.User;
import com.example.task_manager_backend.features.auth.repositories.UserRepository;
import com.example.task_manager_backend.features.projects.core.CreateProjectRequest;
import com.example.task_manager_backend.features.projects.core.ProjectBusinessException;
import com.example.task_manager_backend.features.projects.domain.Project;
import com.example.task_manager_backend.features.projects.domain.ProjectMember;
import com.example.task_manager_backend.features.projects.repositories.ProjectRepository;
import com.example.task_manager_backend.features.projects.repositories.ProjectMemberRepository;
import org.springframework.stereotype.Component;

@Component
public class CreateProjectHandler {
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;

    public CreateProjectHandler(
            ProjectRepository projectRepository,
            ProjectMemberRepository projectMemberRepository,
            UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.userRepository = userRepository;
    }

    public Project execute(CreateProjectRequest request, Long ownerId) {
        // Validar que o owner existe (apenas ativos)
        User owner = userRepository.findByIdAndActiveTrue(ownerId)
                .orElseThrow(() -> new ProjectBusinessException("Owner user not found"));

        // Criar projeto
        Project project = new Project();
        project.setName(request.name());
        project.setDescription(request.description());
        project.setActive(true);
        project.setOwnerId(owner.getId());

        Project savedProject = projectRepository.save(project);

        // Adicionar o owner como membro do projeto (ADMIN)
        ProjectMember ownerMember = new ProjectMember();
        ownerMember.setProject(savedProject);
        ownerMember.setUser(owner);
        ownerMember.setProjectRole("ADMIN");
        ownerMember.setActive(true);

        projectMemberRepository.save(ownerMember);

        return savedProject;
    }
}

