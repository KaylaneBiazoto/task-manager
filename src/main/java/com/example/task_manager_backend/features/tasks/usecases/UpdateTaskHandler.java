package com.example.task_manager_backend.features.tasks.usecases;

import com.example.task_manager_backend.features.auth.domain.User;
import com.example.task_manager_backend.features.auth.repositories.UserRepository;
import com.example.task_manager_backend.features.projects.domain.Project;
import com.example.task_manager_backend.features.projects.repositories.ProjectRepository;
import com.example.task_manager_backend.features.tasks.core.exception.TaskBusinessException;
import com.example.task_manager_backend.features.tasks.core.exception.TaskNotFoundException;
import com.example.task_manager_backend.features.tasks.core.dto.UpdateTaskRequest;
import com.example.task_manager_backend.features.tasks.domain.Task;
import com.example.task_manager_backend.features.tasks.domain.TaskStatus;
import com.example.task_manager_backend.features.tasks.repositories.TaskRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UpdateTaskHandler {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public UpdateTaskHandler(TaskRepository taskRepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public Task execute(UUID taskId, UpdateTaskRequest request, UUID currentUserId, String currentUserRole) {
        Task task = taskRepository.findByIdAndActiveTrue(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        // Se está tentando mudar o status
        if (request.status() != null) {
            validateStatusTransition(task, request.status(), currentUserRole, task.getProject());
        }

        // Se está tentando mudar o assignee
        if (request.assigneeId() != null && (task.getAssignee() == null || !request.assigneeId().equals(task.getAssignee().getId()))) {
            User newAssignee = userRepository.findByIdAndActiveTrue(request.assigneeId())
                    .orElseThrow(() -> new TaskBusinessException("Assignee user not found"));

            validateAssigneeBelongsToProject(task.getProject(), newAssignee);

            // Validar WIP limit para o novo assignee
            long inProgressCount = taskRepository.countInProgressTasksByAssignee(newAssignee.getId(), TaskStatus.IN_PROGRESS);
            if (inProgressCount >= 5 && (request.status() == null || request.status() == TaskStatus.IN_PROGRESS)) {
                throw new TaskBusinessException("New assignee has reached the maximum limit of 5 in-progress tasks (WIP limit exceeded)");
            }

            task.setAssignee(newAssignee);
        }

        if (request.title() != null) {
            task.setTitle(request.title());
        }
        if (request.description() != null) {
            task.setDescription(request.description());
        }
        if (request.priority() != null) {
            task.setPriority(request.priority());
        }
        if (request.deadline() != null) {
            task.setDeadline(request.deadline());
        }
        if (request.status() != null) {
            task.setStatus(request.status());
        }

        return taskRepository.save(task);
    }

    private void validateStatusTransition(Task task, TaskStatus newStatus, String currentUserRole, Project project) {
        TaskStatus currentStatus = task.getStatus();

        // Regra 1: Uma tarefa DONE não pode voltar para TODO (apenas para IN_PROGRESS)
        if (currentStatus == TaskStatus.DONE && newStatus == TaskStatus.TODO) {
            throw new TaskBusinessException("A task with DONE status cannot return to TODO. Only IN_PROGRESS is allowed.");
        }

        // Regra 2: Tarefas com prioridade CRITICAL só podem ser fechadas (DONE) pelo ADMIN do projeto
        if (newStatus == TaskStatus.DONE && task.getPriority().name().equals("CRITICAL")) {
            if (!"ADMIN".equals(currentUserRole)) {
                throw new TaskBusinessException("Only project ADMIN can mark CRITICAL priority tasks as DONE");
            }
        }
    }

    private void validateAssigneeBelongsToProject(Project project, User assignee) {
        boolean isMember = project.getMembers().stream()
                .anyMatch(member -> member.getUser().getId().equals(assignee.getId()) && member.getActive());

        if (!isMember) {
            throw new TaskBusinessException("Assignee is not a member of the project");
        }
    }
}


