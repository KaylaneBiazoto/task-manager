package com.example.task_manager_backend.features.projects.usecases;

import com.example.task_manager_backend.features.auth.domain.User;
import com.example.task_manager_backend.features.auth.repositories.UserRepository;
import com.example.task_manager_backend.features.projects.core.CreateProjectRequest;
import com.example.task_manager_backend.features.projects.core.ProjectBusinessException;
import com.example.task_manager_backend.features.projects.domain.Project;
import com.example.task_manager_backend.features.projects.domain.ProjectMember;
import com.example.task_manager_backend.features.projects.repositories.ProjectMemberRepository;
import com.example.task_manager_backend.features.projects.repositories.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProjectHandlerTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @Mock
    private UserRepository userRepository;

    private CreateProjectHandler handler;

    @BeforeEach
    void setUp() {
        handler = new CreateProjectHandler(projectRepository, projectMemberRepository, userRepository);
    }

    @Test
    void testExecute_WithValidOwner_ShouldCreateProjectAndAddOwnerAsMember() {
        // Arrange
        Long ownerId = 1L;
        String projectName = "Test Project";
        String projectDescription = "Test Description";

        User owner = new User();
        owner.setId(ownerId);
        owner.setUsername("admin");
        owner.setActive(true);

        CreateProjectRequest request = new CreateProjectRequest();
        request.setName(projectName);
        request.setDescription(projectDescription);
        request.setOwnerId(ownerId);

        Project savedProject = new Project();
        savedProject.setId(1L);
        savedProject.setName(projectName);
        savedProject.setDescription(projectDescription);
        savedProject.setOwnerId(ownerId);
        savedProject.setActive(true);

        when(userRepository.findByIdAndActiveTrue(ownerId)).thenReturn(Optional.of(owner));
        when(projectRepository.save(any(Project.class))).thenReturn(savedProject);
        when(projectMemberRepository.save(any(ProjectMember.class))).thenReturn(new ProjectMember());

        // Act
        Project result = handler.execute(request, ownerId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo(projectName);
        assertThat(result.getDescription()).isEqualTo(projectDescription);
        assertThat(result.getActive()).isTrue();

        verify(userRepository).findByIdAndActiveTrue(ownerId);
        verify(projectRepository).save(any(Project.class));
        verify(projectMemberRepository).save(any(ProjectMember.class));
    }

    @Test
    void testExecute_WithInvalidOwner_ShouldThrowProjectBusinessException() {
        // Arrange
        Long invalidOwnerId = 999L;
        CreateProjectRequest request = new CreateProjectRequest();
        request.setName("Test Project");
        request.setOwnerId(invalidOwnerId);

        when(userRepository.findByIdAndActiveTrue(invalidOwnerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> handler.execute(request, invalidOwnerId))
                .isInstanceOf(ProjectBusinessException.class)
                .hasMessage("Owner user not found");

        verify(userRepository).findByIdAndActiveTrue(invalidOwnerId);
        verify(projectRepository, never()).save(any());
        verify(projectMemberRepository, never()).save(any());
    }

    @Test
    void testExecute_WithInactiveOwner_ShouldThrowProjectBusinessException() {
        // Arrange
        Long ownerId = 1L;
        User inactiveOwner = new User();
        inactiveOwner.setId(ownerId);
        inactiveOwner.setActive(false);

        CreateProjectRequest request = new CreateProjectRequest();
        request.setName("Test Project");
        request.setOwnerId(ownerId);

        when(userRepository.findByIdAndActiveTrue(ownerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> handler.execute(request, ownerId))
                .isInstanceOf(ProjectBusinessException.class)
                .hasMessage("Owner user not found");

        verify(projectRepository, never()).save(any());
    }

    @Test
    void testExecute_ShouldSaveProjectMemberWithAdminRole() {
        // Arrange
        Long ownerId = 1L;
        User owner = new User();
        owner.setId(ownerId);
        owner.setUsername("admin");
        owner.setActive(true);

        CreateProjectRequest request = new CreateProjectRequest();
        request.setName("Test Project");
        request.setOwnerId(ownerId);

        Project savedProject = new Project();
        savedProject.setId(1L);
        savedProject.setOwnerId(ownerId);
        savedProject.setActive(true);

        when(userRepository.findByIdAndActiveTrue(ownerId)).thenReturn(Optional.of(owner));
        when(projectRepository.save(any(Project.class))).thenReturn(savedProject);

        // Act
        handler.execute(request, ownerId);

        // Assert
        verify(projectMemberRepository).save(argThat(member ->
                member.getProject().getId().equals(1L) &&
                member.getUser().getId().equals(ownerId) &&
                member.getProjectRole().equals("ADMIN") &&
                member.getActive()
        ));
    }
}

