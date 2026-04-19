package com.example.task_manager_backend.features.projects.usecases;
import java.util.UUID;

import com.example.task_manager_backend.features.projects.core.exception.ProjectBusinessException;
import com.example.task_manager_backend.features.projects.core.exception.ProjectNotFoundException;
import com.example.task_manager_backend.features.projects.domain.Project;
import com.example.task_manager_backend.features.projects.repositories.ProjectRepository;
import com.example.task_manager_backend.features.projects.repositories.ProjectMemberRepository;
import org.springframework.stereotype.Component;

@Component
public class DeleteProjectHandler {
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    public DeleteProjectHandler(ProjectRepository projectRepository, ProjectMemberRepository projectMemberRepository) {
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
    }

    public void execute(UUID projectId, UUID currentUserId) {
        Project project = projectRepository.findByIdAndActiveTrue(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        // Validar que o usuário está autenticado
        if (currentUserId == null) {
            throw new ProjectBusinessException("User must be authenticated to delete a project");
        }

        // Regra: Somente ADMIN do projeto pode deletá-lo
        boolean isProjectAdmin = project.getMembers().stream()
                .anyMatch(member -> member.getUser().getId().equals(currentUserId) 
                        && "ADMIN".equals(member.getProjectRole()) 
                        && member.getActive());

        if (!isProjectAdmin) {
            throw new ProjectBusinessException("Only project ADMIN can delete the project");
        }

        // Regra: Se o projeto tem tarefas ativas, não pode ser deletado
        boolean hasActiveTasks = project.getTasks() != null && project.getTasks().stream()
                .anyMatch(task -> task.getActive());
        
        if (hasActiveTasks) {
            throw new ProjectBusinessException("Cannot delete project with active tasks. Please delete all tasks first.");
        }

        // Regra: Se o projeto tem membros ativos além do ADMIN atual, não pode ser deletado
        boolean hasOtherActiveMembers = project.getMembers() != null && project.getMembers().stream()
                .anyMatch(member -> member.getActive() && !member.getUser().getId().equals(currentUserId));
        
        if (hasOtherActiveMembers) {
            throw new ProjectBusinessException("Cannot delete project with active members. Please remove all members first.");
        }

        project.setActive(false);
        projectRepository.save(project);
    }
}

