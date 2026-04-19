package com.example.task_manager_backend.features.tasks.usecases;

import com.example.task_manager_backend.features.auth.domain.User;
import com.example.task_manager_backend.features.auth.repositories.UserRepository;
import com.example.task_manager_backend.features.projects.domain.Project;
import com.example.task_manager_backend.features.projects.domain.ProjectMember;
import com.example.task_manager_backend.features.projects.repositories.ProjectRepository;
import com.example.task_manager_backend.features.tasks.core.dto.CreateTaskRequest;
import com.example.task_manager_backend.features.tasks.core.exception.TaskBusinessException;
import com.example.task_manager_backend.features.tasks.domain.Task;
import com.example.task_manager_backend.features.tasks.domain.TaskPriority;
import com.example.task_manager_backend.features.tasks.domain.TaskStatus;
import com.example.task_manager_backend.features.tasks.repositories.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTaskHandlerTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    private CreateTaskHandler handler;

    @BeforeEach
    void setUp() {
        handler = new CreateTaskHandler(taskRepository, projectRepository, userRepository);
    }

    @Test
    void testExecute_WithValidRequest_ShouldCreateTask() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        UUID assigneeId = UUID.randomUUID();
        UUID taskId = UUID.randomUUID();
        String taskTitle = "Test Task";
        TaskPriority priority = TaskPriority.HIGH;
        LocalDate deadline = LocalDate.now().plusDays(7);

        User assignee = new User();
        assignee.setId(assigneeId);
        assignee.setUsername("user2");
        assignee.setActive(true);

        Project project = new Project();
        project.setId(projectId);
        project.setName("Test Project");
        project.setActive(true);

        ProjectMember member = new ProjectMember();
        member.setUser(assignee);
        member.setActive(true);

        project.setMembers(List.of(member));

        CreateTaskRequest request = new CreateTaskRequest(taskTitle, "Test Description", priority, deadline, projectId, assigneeId);

        Task savedTask = new Task();
        savedTask.setId(taskId);
        savedTask.setTitle(taskTitle);
        savedTask.setProject(project);
        savedTask.setAssignee(assignee);
        savedTask.setStatus(TaskStatus.TODO);
        savedTask.setActive(true);

        when(projectRepository.findByIdAndActiveTrue(projectId)).thenReturn(Optional.of(project));
        when(userRepository.findByIdAndActiveTrue(assigneeId)).thenReturn(Optional.of(assignee));
        when(taskRepository.countInProgressTasksByAssignee(assigneeId, TaskStatus.IN_PROGRESS)).thenReturn(0L);
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        // Act
        Task result = handler.execute(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(taskId);
        assertThat(result.getTitle()).isEqualTo(taskTitle);
        assertThat(result.getStatus()).isEqualTo(TaskStatus.TODO);
        assertThat(result.getActive()).isTrue();

        verify(projectRepository).findByIdAndActiveTrue(projectId);
        verify(userRepository).findByIdAndActiveTrue(assigneeId);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void testExecute_WithInvalidProject_ShouldThrowTaskBusinessException() {
        // Arrange
        UUID invalidProjectId = UUID.randomUUID();
        UUID assigneeId = UUID.randomUUID();
        CreateTaskRequest request = new CreateTaskRequest(null, null, null, null, invalidProjectId, assigneeId);

        when(projectRepository.findByIdAndActiveTrue(invalidProjectId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> handler.execute(request))
                .isInstanceOf(TaskBusinessException.class)
                .hasMessage("Project not found");

        verify(projectRepository).findByIdAndActiveTrue(invalidProjectId);
        verify(userRepository, never()).findByIdAndActiveTrue(any(UUID.class));
        verify(taskRepository, never()).save(any());
    }

    @Test
    void testExecute_WithInvalidAssignee_ShouldThrowTaskBusinessException() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        UUID invalidAssigneeId = UUID.randomUUID();

        Project project = new Project();
        project.setId(projectId);
        project.setActive(true);

        CreateTaskRequest request = new CreateTaskRequest(null, null, null, null, projectId, invalidAssigneeId);

        when(projectRepository.findByIdAndActiveTrue(projectId)).thenReturn(Optional.of(project));
        when(userRepository.findByIdAndActiveTrue(invalidAssigneeId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> handler.execute(request))
                .isInstanceOf(TaskBusinessException.class)
                .hasMessage("Assignee user not found");

        verify(taskRepository, never()).save(any());
    }

    @Test
    void testExecute_WithAssigneeNotProjectMember_ShouldThrowTaskBusinessException() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        UUID assigneeId = UUID.randomUUID();

        User assignee = new User();
        assignee.setId(assigneeId);
        assignee.setActive(true);

        Project project = new Project();
        project.setId(projectId);
        project.setActive(true);
        project.setMembers(Collections.emptyList());

        CreateTaskRequest request = new CreateTaskRequest(null, null, null, null, projectId, assigneeId);

        when(projectRepository.findByIdAndActiveTrue(projectId)).thenReturn(Optional.of(project));
        when(userRepository.findByIdAndActiveTrue(assigneeId)).thenReturn(Optional.of(assignee));

        // Act & Assert
        assertThatThrownBy(() -> handler.execute(request))
                .isInstanceOf(TaskBusinessException.class)
                .hasMessage("Assignee is not a member of the project");

        verify(taskRepository, never()).save(any());
    }

    @Test
    void testExecute_WithWipLimitExceeded_ShouldThrowTaskBusinessException() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        UUID assigneeId = UUID.randomUUID();

        User assignee = new User();
        assignee.setId(assigneeId);
        assignee.setActive(true);

        Project project = new Project();
        project.setId(projectId);
        project.setActive(true);

        ProjectMember member = new ProjectMember();
        member.setUser(assignee);
        member.setActive(true);

        project.setMembers(List.of(member));

        CreateTaskRequest request = new CreateTaskRequest(null, null, null, null, projectId, assigneeId);

        when(projectRepository.findByIdAndActiveTrue(projectId)).thenReturn(Optional.of(project));
        when(userRepository.findByIdAndActiveTrue(assigneeId)).thenReturn(Optional.of(assignee));
        when(taskRepository.countInProgressTasksByAssignee(assigneeId, TaskStatus.IN_PROGRESS)).thenReturn(5L);

        // Act & Assert
        assertThatThrownBy(() -> handler.execute(request))
                .isInstanceOf(TaskBusinessException.class)
                .hasMessage("Assignee has reached the maximum limit of 5 in-progress tasks (WIP limit exceeded)");

        verify(taskRepository, never()).save(any());
    }

    @Test
    void testExecute_WithAssigneeHaving4InProgressTasks_ShouldCreateTask() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        UUID assigneeId = UUID.randomUUID();
        UUID taskId = UUID.randomUUID();

        User assignee = new User();
        assignee.setId(assigneeId);
        assignee.setActive(true);

        Project project = new Project();
        project.setId(projectId);
        project.setActive(true);

        ProjectMember member = new ProjectMember();
        member.setUser(assignee);
        member.setActive(true);

        project.setMembers(List.of(member));

        CreateTaskRequest request = new CreateTaskRequest("New Task", null, null, null, projectId, assigneeId);

        Task savedTask = new Task();
        savedTask.setId(taskId);
        savedTask.setTitle("New Task");
        savedTask.setStatus(TaskStatus.TODO);
        savedTask.setActive(true);

        when(projectRepository.findByIdAndActiveTrue(projectId)).thenReturn(Optional.of(project));
        when(userRepository.findByIdAndActiveTrue(assigneeId)).thenReturn(Optional.of(assignee));
        when(taskRepository.countInProgressTasksByAssignee(assigneeId, TaskStatus.IN_PROGRESS)).thenReturn(4L);
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        // Act
        Task result = handler.execute(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(taskId);
        verify(taskRepository).save(any(Task.class));
    }
}

