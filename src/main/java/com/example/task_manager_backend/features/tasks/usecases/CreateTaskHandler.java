package com.example.task_manager_backend.features.tasks.usecases;

import com.example.task_manager_backend.features.auth.domain.User;
import com.example.task_manager_backend.features.auth.repositories.UserRepository;
import com.example.task_manager_backend.features.projects.domain.Project;
import com.example.task_manager_backend.features.projects.repositories.ProjectRepository;
import com.example.task_manager_backend.features.tasks.core.CreateTaskRequest;
import com.example.task_manager_backend.features.tasks.core.TaskBusinessException;
import com.example.task_manager_backend.features.tasks.domain.Task;
import com.example.task_manager_backend.features.tasks.domain.TaskStatus;
import com.example.task_manager_backend.features.tasks.repositories.TaskRepository;
import org.springframework.stereotype.Component;

@Component
public class CreateTaskHandler {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public CreateTaskHandler(TaskRepository taskRepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public Task execute(CreateTaskRequest request) {
        // Validar projeto existe
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new TaskBusinessException("Project not found"));

        // Validar assignee existe
        User assignee = userRepository.findById(request.getAssigneeId())
                .orElseThrow(() -> new TaskBusinessException("Assignee user not found"));

        // Validar se assignee é membro do projeto
        validateAssigneeBelongsToProject(project, assignee);

        // Validar WIP limit: máximo 5 tarefas IN_PROGRESS por responsável
        long inProgressCount = taskRepository.countInProgressTasksByAssignee(assignee.getId(), TaskStatus.IN_PROGRESS);
        if (inProgressCount >= 5) {
            throw new TaskBusinessException("Assignee has reached the maximum limit of 5 in-progress tasks (WIP limit exceeded)");
        }

        // Criar tarefa
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(TaskStatus.TODO);
        task.setPriority(request.getPriority());
        task.setDeadline(request.getDeadline());
        task.setProject(project);
        task.setAssignee(assignee);
        task.setActive(true);

        return taskRepository.save(task);
    }

    private void validateAssigneeBelongsToProject(Project project, User assignee) {
        boolean isMember = project.getMembers().stream()
                .anyMatch(member -> member.getUser().getId().equals(assignee.getId()) && member.getActive());

        if (!isMember) {
            throw new TaskBusinessException("Assignee is not a member of the project");
        }
    }
}


