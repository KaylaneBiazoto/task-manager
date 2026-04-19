package com.example.task_manager_backend.features.projects.usecases;

import com.example.task_manager_backend.features.auth.domain.User;
import com.example.task_manager_backend.features.auth.repositories.UserRepository;
import com.example.task_manager_backend.features.projects.core.AddProjectMemberRequest;
import com.example.task_manager_backend.features.projects.core.ProjectBusinessException;
import com.example.task_manager_backend.features.projects.core.ProjectNotFoundException;
import com.example.task_manager_backend.features.projects.domain.Project;
import com.example.task_manager_backend.features.projects.domain.ProjectMember;
import com.example.task_manager_backend.features.projects.repositories.ProjectMemberRepository;
import com.example.task_manager_backend.features.projects.repositories.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddProjectMemberHandlerTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @Mock
    private UserRepository userRepository;

    private AddProjectMemberHandler handler;

    @BeforeEach
    void setUp() {
        handler = new AddProjectMemberHandler(projectRepository, projectMemberRepository, userRepository);
    }

    @Test
    void testExecute_WithValidData_ShouldAddMember() {
        // Arrange
        Long projectId = 1L;
        Long userId = 2L;
        String memberRole = "MEMBER";

        User user = new User();
        user.setId(userId);
        user.setUsername("bob");
        user.setActive(true);

        Project project = new Project();
        project.setId(projectId);
        project.setName("Test Project");
        project.setActive(true);
        project.setMembers(new ArrayList<>()); // No members yet

        AddProjectMemberRequest request = new AddProjectMemberRequest(userId, memberRole);

        ProjectMember savedMember = new ProjectMember();
        savedMember.setId(1L);
        savedMember.setProject(project);
        savedMember.setUser(user);
        savedMember.setProjectRole(memberRole);
        savedMember.setActive(true);

        when(projectRepository.findByIdAndActiveTrue(projectId)).thenReturn(Optional.of(project));
        when(userRepository.findByIdAndActiveTrue(userId)).thenReturn(Optional.of(user));
        when(projectMemberRepository.save(any(ProjectMember.class))).thenReturn(savedMember);

        // Act
        ProjectMember result = handler.execute(projectId, request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUser().getId()).isEqualTo(userId);
        assertThat(result.getProjectRole()).isEqualTo(memberRole);
        assertThat(result.getActive()).isTrue();

        verify(projectRepository).findByIdAndActiveTrue(projectId);
        verify(userRepository).findByIdAndActiveTrue(userId);
        verify(projectMemberRepository).save(any(ProjectMember.class));
    }

    @Test
    void testExecute_WithInvalidProject_ShouldThrowProjectNotFoundException() {
        // Arrange
        Long invalidProjectId = 999L;
        AddProjectMemberRequest request = new AddProjectMemberRequest(2L, null);

        when(projectRepository.findByIdAndActiveTrue(invalidProjectId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> handler.execute(invalidProjectId, request))
                .isInstanceOf(ProjectNotFoundException.class)
                .hasMessage("Project not found");

        verify(userRepository, never()).findByIdAndActiveTrue(any());
        verify(projectMemberRepository, never()).save(any());
    }

    @Test
    void testExecute_WithInvalidUser_ShouldThrowProjectBusinessException() {
        // Arrange
        Long projectId = 1L;
        Long invalidUserId = 999L;

        Project project = new Project();
        project.setId(projectId);
        project.setActive(true);

        AddProjectMemberRequest request = new AddProjectMemberRequest(invalidUserId, null);

        when(projectRepository.findByIdAndActiveTrue(projectId)).thenReturn(Optional.of(project));
        when(userRepository.findByIdAndActiveTrue(invalidUserId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> handler.execute(projectId, request))
                .isInstanceOf(ProjectBusinessException.class)
                .hasMessage("User not found");

        verify(projectMemberRepository, never()).save(any());
    }

    @Test
    void testExecute_WithUserAlreadyMember_ShouldThrowProjectBusinessException() {
        // Arrange
        Long projectId = 1L;
        Long userId = 2L;

        User user = new User();
        user.setId(userId);
        user.setActive(true);

        ProjectMember existingMember = new ProjectMember();
        existingMember.setUser(user);
        existingMember.setActive(true);

        Project project = new Project();
        project.setId(projectId);
        project.setActive(true);
        project.setMembers(List.of(existingMember));

        AddProjectMemberRequest request = new AddProjectMemberRequest(userId, "MEMBER");

        when(projectRepository.findByIdAndActiveTrue(projectId)).thenReturn(Optional.of(project));
        when(userRepository.findByIdAndActiveTrue(userId)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThatThrownBy(() -> handler.execute(projectId, request))
                .isInstanceOf(ProjectBusinessException.class)
                .hasMessage("User is already a member of this project");

        verify(projectMemberRepository, never()).save(any());
    }

    @Test
    void testExecute_WithUserBeingAdminRole_ShouldSucceed() {
        // Arrange
        Long projectId = 1L;
        Long userId = 2L;

        User user = new User();
        user.setId(userId);
        user.setActive(true);

        Project project = new Project();
        project.setId(projectId);
        project.setActive(true);
        project.setMembers(new ArrayList<>());

        AddProjectMemberRequest request = new AddProjectMemberRequest(userId, "ADMIN");

        ProjectMember savedMember = new ProjectMember();
        savedMember.setId(1L);
        savedMember.setProjectRole("ADMIN");
        savedMember.setActive(true);

        when(projectRepository.findByIdAndActiveTrue(projectId)).thenReturn(Optional.of(project));
        when(userRepository.findByIdAndActiveTrue(userId)).thenReturn(Optional.of(user));
        when(projectMemberRepository.save(any(ProjectMember.class))).thenReturn(savedMember);

        // Act
        ProjectMember result = handler.execute(projectId, request);

        // Assert
        assertThat(result.getProjectRole()).isEqualTo("ADMIN");
        verify(projectMemberRepository).save(any(ProjectMember.class));
    }

    @Test
    void testExecute_WithInactiveUser_ShouldThrowProjectBusinessException() {
        // Arrange
        Long projectId = 1L;
        Long inactiveUserId = 2L;

        Project project = new Project();
        project.setId(projectId);
        project.setActive(true);

        User inactiveUser = new User();
        inactiveUser.setId(inactiveUserId);
        inactiveUser.setActive(false);

        AddProjectMemberRequest request = new AddProjectMemberRequest(inactiveUserId, null);

        when(projectRepository.findByIdAndActiveTrue(projectId)).thenReturn(Optional.of(project));
        when(userRepository.findByIdAndActiveTrue(inactiveUserId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> handler.execute(projectId, request))
                .isInstanceOf(ProjectBusinessException.class)
                .hasMessage("User not found");

        verify(projectMemberRepository, never()).save(any());
    }

    @Test
    void testExecute_ShouldNotCountInactiveMembers() {
        // Arrange
        Long projectId = 1L;
        Long userId = 2L;

        User user = new User();
        user.setId(userId);
        user.setActive(true);

        ProjectMember inactiveMember = new ProjectMember();
        inactiveMember.setUser(user);
        inactiveMember.setActive(false); // Inactive

        Project project = new Project();
        project.setId(projectId);
        project.setActive(true);
        project.setMembers(List.of(inactiveMember)); // User is inactive member

        AddProjectMemberRequest request = new AddProjectMemberRequest(userId, "MEMBER");

        ProjectMember savedMember = new ProjectMember();
        savedMember.setId(2L);
        savedMember.setActive(true);

        when(projectRepository.findByIdAndActiveTrue(projectId)).thenReturn(Optional.of(project));
        when(userRepository.findByIdAndActiveTrue(userId)).thenReturn(Optional.of(user));
        when(projectMemberRepository.save(any(ProjectMember.class))).thenReturn(savedMember);

        // Act
        ProjectMember result = handler.execute(projectId, request);

        // Assert
        assertThat(result).isNotNull();
        verify(projectMemberRepository).save(any(ProjectMember.class));
    }
}

