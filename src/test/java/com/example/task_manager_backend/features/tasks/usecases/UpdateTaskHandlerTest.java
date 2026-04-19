package com.example.task_manager_backend.features.tasks.usecases;

import com.example.task_manager_backend.features.auth.domain.User;
import com.example.task_manager_backend.features.auth.repositories.UserRepository;
import com.example.task_manager_backend.features.projects.domain.Project;
import com.example.task_manager_backend.features.projects.domain.ProjectMember;
import com.example.task_manager_backend.features.projects.repositories.ProjectRepository;
import com.example.task_manager_backend.features.tasks.core.TaskBusinessException;
import com.example.task_manager_backend.features.tasks.core.TaskNotFoundException;
import com.example.task_manager_backend.features.tasks.core.UpdateTaskRequest;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateTaskHandlerTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    private UpdateTaskHandler handler;

    @BeforeEach
    void setUp() {
        handler = new UpdateTaskHandler(taskRepository, projectRepository, userRepository);
    }

    @Test
    void testExecute_UpdateTaskStatus_ShouldSucceed() {
        // Arrange
        Long taskId = 1L;
        Long assigneeId = 2L;

        User assignee = new User();
        assignee.setId(assigneeId);
        assignee.setActive(true);

        Project project = new Project();
        project.setId(1L);
        project.setActive(true);

        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Test Task");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.HIGH);
        task.setAssignee(assignee);
        task.setProject(project);
        task.setActive(true);

        UpdateTaskRequest request = new UpdateTaskRequest(null, null, TaskStatus.IN_PROGRESS, null, null, null);

        Task updatedTask = new Task();
        updatedTask.setId(taskId);
        updatedTask.setStatus(TaskStatus.IN_PROGRESS);
        updatedTask.setTitle("Test Task");

        when(taskRepository.findByIdAndActiveTrue(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        // Act
        Task result = handler.execute(taskId, request, 1L, "ADMIN");

        // Assert
        assertThat(result.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void testExecute_WithTaskNotFound_ShouldThrowTaskNotFoundException() {
        // Arrange
        Long invalidTaskId = 999L;
        UpdateTaskRequest request = new UpdateTaskRequest(null, null, null, null, null, null);

        when(taskRepository.findByIdAndActiveTrue(invalidTaskId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> handler.execute(invalidTaskId, request, 1L, "ADMIN"))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage("Task not found");

        verify(taskRepository, never()).save(any());
    }

    @Test
    void testExecute_DoneTaskToTodo_ShouldThrowTaskBusinessException() {
        // Arrange
        Long taskId = 1L;
        User assignee = new User();
        assignee.setId(1L);

        Project project = new Project();
        project.setId(1L);

        Task task = new Task();
        task.setId(taskId);
        task.setStatus(TaskStatus.DONE);
        task.setPriority(TaskPriority.HIGH);
        task.setAssignee(assignee);
        task.setProject(project);
        task.setActive(true);

        UpdateTaskRequest request = new UpdateTaskRequest(null, null, TaskStatus.TODO, null, null, null);

        when(taskRepository.findByIdAndActiveTrue(taskId)).thenReturn(Optional.of(task));

        // Act & Assert
        assertThatThrownBy(() -> handler.execute(taskId, request, 1L, "ADMIN"))
                .isInstanceOf(TaskBusinessException.class)
                .hasMessage("A task with DONE status cannot return to TODO. Only IN_PROGRESS is allowed.");

        verify(taskRepository, never()).save(any());
    }

    @Test
    void testExecute_DoneTaskToInProgress_ShouldSucceed() {
        // Arrange
        Long taskId = 1L;
        User assignee = new User();
        assignee.setId(1L);

        Project project = new Project();
        project.setId(1L);

        Task task = new Task();
        task.setId(taskId);
        task.setStatus(TaskStatus.DONE);
        task.setPriority(TaskPriority.HIGH);
        task.setAssignee(assignee);
        task.setProject(project);
        task.setActive(true);

        UpdateTaskRequest request = new UpdateTaskRequest(null, null, TaskStatus.IN_PROGRESS, null, null, null);

        Task updatedTask = new Task();
        updatedTask.setStatus(TaskStatus.IN_PROGRESS);

        when(taskRepository.findByIdAndActiveTrue(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        // Act
        Task result = handler.execute(taskId, request, 1L, "ADMIN");

        // Assert
        assertThat(result.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void testExecute_CriticalTaskClosedByAdmin_ShouldSucceed() {
        // Arrange
        Long taskId = 1L;
        User assignee = new User();
        assignee.setId(1L);

        Project project = new Project();
        project.setId(1L);

        Task task = new Task();
        task.setId(taskId);
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setPriority(TaskPriority.CRITICAL);
        task.setAssignee(assignee);
        task.setProject(project);
        task.setActive(true);

        UpdateTaskRequest request = new UpdateTaskRequest(null, null, TaskStatus.DONE, null, null, null);

        Task updatedTask = new Task();
        updatedTask.setStatus(TaskStatus.DONE);

        when(taskRepository.findByIdAndActiveTrue(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        // Act
        Task result = handler.execute(taskId, request, 1L, "ADMIN");

        // Assert
        assertThat(result.getStatus()).isEqualTo(TaskStatus.DONE);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void testExecute_CriticalTaskClosedByMember_ShouldThrowTaskBusinessException() {
        // Arrange
        Long taskId = 1L;
        User assignee = new User();
        assignee.setId(1L);

        Project project = new Project();
        project.setId(1L);

        Task task = new Task();
        task.setId(taskId);
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setPriority(TaskPriority.CRITICAL);
        task.setAssignee(assignee);
        task.setProject(project);
        task.setActive(true);

        UpdateTaskRequest request = new UpdateTaskRequest(null, null, TaskStatus.DONE, null, null, null);

        when(taskRepository.findByIdAndActiveTrue(taskId)).thenReturn(Optional.of(task));

        // Act & Assert
        assertThatThrownBy(() -> handler.execute(taskId, request, 1L, "MEMBER"))
                .isInstanceOf(TaskBusinessException.class)
                .hasMessage("Only project ADMIN can mark CRITICAL priority tasks as DONE");

        verify(taskRepository, never()).save(any());
    }

    @Test
    void testExecute_ChangeAssignee_ShouldSucceed() {
        // Arrange
        Long taskId = 1L;
        Long oldAssigneeId = 1L;
        Long newAssigneeId = 2L;

        User oldAssignee = new User();
        oldAssignee.setId(oldAssigneeId);

        User newAssignee = new User();
        newAssignee.setId(newAssigneeId);
        newAssignee.setActive(true);

        Project project = new Project();
        project.setId(1L);

        ProjectMember memberNew = new ProjectMember();
        memberNew.setUser(newAssignee);
        memberNew.setActive(true);

        project.setMembers(List.of(memberNew));

        Task task = new Task();
        task.setId(taskId);
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.HIGH);
        task.setAssignee(oldAssignee);
        task.setProject(project);
        task.setActive(true);

        UpdateTaskRequest request = new UpdateTaskRequest(null, null, null, null, null, newAssigneeId);

        Task updatedTask = new Task();
        updatedTask.setAssignee(newAssignee);

        when(taskRepository.findByIdAndActiveTrue(taskId)).thenReturn(Optional.of(task));
        when(userRepository.findByIdAndActiveTrue(newAssigneeId)).thenReturn(Optional.of(newAssignee));
        when(taskRepository.countInProgressTasksByAssignee(newAssigneeId, TaskStatus.IN_PROGRESS)).thenReturn(0L);
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        // Act
        Task result = handler.execute(taskId, request, 1L, "ADMIN");

        // Assert
        assertThat(result.getAssignee()).isEqualTo(newAssignee);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void testExecute_ChangeAssigneeNotProjectMember_ShouldThrowTaskBusinessException() {
        // Arrange
        Long taskId = 1L;
        Long newAssigneeId = 2L;

        User oldAssignee = new User();
        oldAssignee.setId(1L);

        User newAssignee = new User();
        newAssignee.setId(newAssigneeId);
        newAssignee.setActive(true);

        Project project = new Project();
        project.setId(1L);
        project.setMembers(List.of()); // New assignee is not a member

        Task task = new Task();
        task.setId(taskId);
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.HIGH);
        task.setAssignee(oldAssignee);
        task.setProject(project);
        task.setActive(true);

        UpdateTaskRequest request = new UpdateTaskRequest(null, null, null, null, null, newAssigneeId);

        when(taskRepository.findByIdAndActiveTrue(taskId)).thenReturn(Optional.of(task));
        when(userRepository.findByIdAndActiveTrue(newAssigneeId)).thenReturn(Optional.of(newAssignee));

        // Act & Assert
        assertThatThrownBy(() -> handler.execute(taskId, request, 1L, "ADMIN"))
                .isInstanceOf(TaskBusinessException.class)
                .hasMessage("Assignee is not a member of the project");

        verify(taskRepository, never()).save(any());
    }

    @Test
    void testExecute_UpdateMultipleFields_ShouldSucceed() {
        // Arrange
        Long taskId = 1L;
        String newTitle = "Updated Title";
        TaskPriority newPriority = TaskPriority.CRITICAL;
        LocalDate newDeadline = LocalDate.now().plusDays(15);

        User assignee = new User();
        assignee.setId(1L);

        Project project = new Project();
        project.setId(1L);

        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Old Title");
        task.setPriority(TaskPriority.HIGH);
        task.setDeadline(LocalDate.now().plusDays(7));
        task.setStatus(TaskStatus.TODO);
        task.setAssignee(assignee);
        task.setProject(project);
        task.setActive(true);

        UpdateTaskRequest request = new UpdateTaskRequest(newTitle, null, null, newPriority, newDeadline, null);

        Task updatedTask = new Task();
        updatedTask.setTitle(newTitle);
        updatedTask.setPriority(newPriority);
        updatedTask.setDeadline(newDeadline);

        when(taskRepository.findByIdAndActiveTrue(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        // Act
        Task result = handler.execute(taskId, request, 1L, "ADMIN");

        // Assert
        assertThat(result.getTitle()).isEqualTo(newTitle);
        assertThat(result.getPriority()).isEqualTo(newPriority);
        assertThat(result.getDeadline()).isEqualTo(newDeadline);
        verify(taskRepository).save(any(Task.class));
    }
}

