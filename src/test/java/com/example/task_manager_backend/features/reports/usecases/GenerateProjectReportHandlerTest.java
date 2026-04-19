package com.example.task_manager_backend.features.reports.usecases;

import com.example.task_manager_backend.features.projects.domain.Project;
import com.example.task_manager_backend.features.projects.repositories.ProjectRepository;
import com.example.task_manager_backend.features.reports.core.dto.TaskReportDto;
import com.example.task_manager_backend.features.reports.core.exception.ProjectNotFoundException;
import com.example.task_manager_backend.features.tasks.repositories.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenerateProjectReportHandlerTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TaskRepository taskRepository;

    private GenerateProjectReportHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GenerateProjectReportHandler(projectRepository, taskRepository);
    }

    @Test
    void testExecute_WithValidProject_ShouldReturnReport() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        Project project = new Project();
        project.setId(projectId);
        project.setName("Test Project");
        project.setActive(true);

        // Mock task counts by status
        List<Object[]> statusCounts = List.of(
                new Object[]{"TODO", 5L},
                new Object[]{"IN_PROGRESS", 3L},
                new Object[]{"DONE", 10L}
        );

        // Mock task counts by priority
        List<Object[]> priorityCounts = List.of(
                new Object[]{"LOW", 8L},
                new Object[]{"MEDIUM", 7L},
                new Object[]{"HIGH", 2L},
                new Object[]{"CRITICAL", 1L}
        );

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(taskRepository.countTasksByStatus(projectId)).thenReturn(statusCounts);
        when(taskRepository.countTasksByPriority(projectId)).thenReturn(priorityCounts);

        // Act
        TaskReportDto result = handler.execute(projectId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.projectId()).isEqualTo(projectId);
        assertThat(result.byStatus()).hasSize(3);
        assertThat(result.byStatus()).containsEntry("TODO", 5L);
        assertThat(result.byStatus()).containsEntry("IN_PROGRESS", 3L);
        assertThat(result.byStatus()).containsEntry("DONE", 10L);

        assertThat(result.byPriority()).hasSize(4);
        assertThat(result.byPriority()).containsEntry("LOW", 8L);
        assertThat(result.byPriority()).containsEntry("MEDIUM", 7L);
        assertThat(result.byPriority()).containsEntry("HIGH", 2L);
        assertThat(result.byPriority()).containsEntry("CRITICAL", 1L);

        verify(projectRepository).findById(projectId);
        verify(taskRepository).countTasksByStatus(projectId);
        verify(taskRepository).countTasksByPriority(projectId);
    }

    @Test
    void testExecute_WithInvalidProject_ShouldThrowProjectNotFoundException() {
        // Arrange
        UUID invalidProjectId = UUID.randomUUID();
        when(projectRepository.findById(invalidProjectId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> handler.execute(invalidProjectId))
                .isInstanceOf(ProjectNotFoundException.class)
                .hasMessageContaining("Projeto não encontrado");

        verify(projectRepository).findById(invalidProjectId);
        verify(taskRepository, never()).countTasksByStatus(any());
        verify(taskRepository, never()).countTasksByPriority(any());
    }

    @Test
    void testExecute_WithEmptyTasks_ShouldReturnEmptyReport() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        Project project = new Project();
        project.setId(projectId);
        project.setActive(true);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(taskRepository.countTasksByStatus(projectId)).thenReturn(List.of());
        when(taskRepository.countTasksByPriority(projectId)).thenReturn(List.of());

        // Act
        TaskReportDto result = handler.execute(projectId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.projectId()).isEqualTo(projectId);
        assertThat(result.byStatus()).isEmpty();
        assertThat(result.byPriority()).isEmpty();
    }
}

