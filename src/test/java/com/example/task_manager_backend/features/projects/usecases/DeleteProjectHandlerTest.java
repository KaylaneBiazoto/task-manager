package com.example.task_manager_backend.features.projects.usecases;

import com.example.task_manager_backend.features.projects.core.exception.ProjectBusinessException;
import com.example.task_manager_backend.features.projects.domain.Project;
import com.example.task_manager_backend.features.projects.domain.ProjectMember;
import com.example.task_manager_backend.features.tasks.domain.Task;
import com.example.task_manager_backend.features.auth.domain.User;
import com.example.task_manager_backend.features.projects.repositories.ProjectRepository;
import com.example.task_manager_backend.features.projects.repositories.ProjectMemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteProjectHandlerTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @InjectMocks
    private DeleteProjectHandler deleteProjectHandler;

    @Test
    void testExecute_DeleteProjectWithActiveTasks_ShouldThrowException() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        UUID currentUserId = UUID.randomUUID();

        User adminUser = new User();
        adminUser.setId(currentUserId);
        adminUser.setRole("ADMIN");
        adminUser.setActive(true);

        ProjectMember adminMember = new ProjectMember();
        adminMember.setUser(adminUser);
        adminMember.setProjectRole("ADMIN");
        adminMember.setActive(true);

        Task activeTask = new Task();
        activeTask.setActive(true);

        List<Task> activeTasks = new ArrayList<>();
        activeTasks.add(activeTask);

        Project project = new Project();
        project.setId(projectId);
        project.setActive(true);
        project.setMembers(List.of(adminMember));
        project.setTasks(activeTasks);

        when(projectRepository.findByIdAndActiveTrue(projectId)).thenReturn(Optional.of(project));

        // Act & Assert
        assertThatThrownBy(() -> deleteProjectHandler.execute(projectId, currentUserId))
                .isInstanceOf(ProjectBusinessException.class)
                .hasMessage("Cannot delete project with active tasks. Please delete all tasks first.");

        verify(projectRepository, never()).save(any());
    }

    @Test
    void testExecute_DeleteProjectWithActiveMembers_ShouldThrowException() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        UUID currentUserId = UUID.randomUUID();

        User adminUser = new User();
        adminUser.setId(currentUserId);
        adminUser.setRole("ADMIN");
        adminUser.setActive(true);

        User memberUser = new User();
        memberUser.setId(UUID.randomUUID());
        memberUser.setActive(true);

        ProjectMember adminMember = new ProjectMember();
        adminMember.setUser(adminUser);
        adminMember.setProjectRole("ADMIN");
        adminMember.setActive(true);

        ProjectMember regularMember = new ProjectMember();
        regularMember.setUser(memberUser);
        regularMember.setProjectRole("MEMBER");
        regularMember.setActive(true);

        Project project = new Project();
        project.setId(projectId);
        project.setActive(true);
        project.setMembers(List.of(adminMember, regularMember));
        project.setTasks(new ArrayList<>());

        when(projectRepository.findByIdAndActiveTrue(projectId)).thenReturn(Optional.of(project));

        // Act & Assert
        assertThatThrownBy(() -> deleteProjectHandler.execute(projectId, currentUserId))
                .isInstanceOf(ProjectBusinessException.class)
                .hasMessage("Cannot delete project with active members. Please remove all members first.");

        verify(projectRepository, never()).save(any());
    }

    @Test
    void testExecute_DeleteProjectWithNonAdminUser_ShouldThrowException() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        UUID currentUserId = UUID.randomUUID();

        User memberUser = new User();
        memberUser.setId(currentUserId);
        memberUser.setRole("MEMBER");
        memberUser.setActive(true);

        ProjectMember memberProjectMember = new ProjectMember();
        memberProjectMember.setUser(memberUser);
        memberProjectMember.setProjectRole("MEMBER");
        memberProjectMember.setActive(true);

        Project project = new Project();
        project.setId(projectId);
        project.setActive(true);
        project.setMembers(List.of(memberProjectMember));
        project.setTasks(new ArrayList<>());

        when(projectRepository.findByIdAndActiveTrue(projectId)).thenReturn(Optional.of(project));

        // Act & Assert
        assertThatThrownBy(() -> deleteProjectHandler.execute(projectId, currentUserId))
                .isInstanceOf(ProjectBusinessException.class)
                .hasMessage("Only project ADMIN can delete the project");

        verify(projectRepository, never()).save(any());
    }

    @Test
    void testExecute_DeleteProjectWithAdminAndNoActiveDependencies_ShouldSucceed() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        UUID currentUserId = UUID.randomUUID();

        User adminUser = new User();
        adminUser.setId(currentUserId);
        adminUser.setRole("ADMIN");
        adminUser.setActive(true);

        ProjectMember adminMember = new ProjectMember();
        adminMember.setUser(adminUser);
        adminMember.setProjectRole("ADMIN");
        adminMember.setActive(true); // Admin member must be active

        Project project = new Project();
        project.setId(projectId);
        project.setActive(true);
        project.setMembers(List.of(adminMember));
        project.setTasks(new ArrayList<>());

        when(projectRepository.findByIdAndActiveTrue(projectId)).thenReturn(Optional.of(project));

        // Act
        deleteProjectHandler.execute(projectId, currentUserId);

        // Assert
        assertThat(project.getActive()).isFalse();
        verify(projectRepository).save(project);
    }
}



