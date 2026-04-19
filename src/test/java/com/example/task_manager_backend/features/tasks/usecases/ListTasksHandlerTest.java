package com.example.task_manager_backend.features.tasks.usecases;

import com.example.task_manager_backend.features.tasks.domain.Task;
import com.example.task_manager_backend.features.tasks.domain.TaskPriority;
import com.example.task_manager_backend.features.tasks.domain.TaskStatus;
import com.example.task_manager_backend.features.tasks.repositories.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListTasksHandlerTest {

    @Mock
    private TaskRepository taskRepository;

    private ListTasksHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ListTasksHandler(taskRepository);
    }

    @Test
    void testExecuteByProjectWithFilters_WithoutFilters_ShouldReturnAllTasks() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);

        Task task1 = new Task();
        task1.setId(UUID.randomUUID());
        task1.setTitle("Task 1");
        task1.setStatus(TaskStatus.TODO);
        task1.setActive(true);

        Task task2 = new Task();
        task2.setId(UUID.randomUUID());
        task2.setTitle("Task 2");
        task2.setStatus(TaskStatus.IN_PROGRESS);
        task2.setActive(true);

        Page<Task> expectedPage = new PageImpl<>(List.of(task1, task2), pageable, 2);

        when(taskRepository.findByProjectIdWithFilters(
                projectId, "", null, null, null, pageable
        )).thenReturn(expectedPage);

        // Act
        Page<Task> result = handler.executeByProjectWithFilters(
                projectId, null, null, null, null, pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).hasSize(2);
        verify(taskRepository).findByProjectIdWithFilters(
                projectId, "", null, null, null, pageable);
    }

    @Test
    void testExecuteByProjectWithFilters_WithStatusFilter_ShouldReturnFilteredTasks() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);

        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setTitle("In Progress Task");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setActive(true);

        Page<Task> expectedPage = new PageImpl<>(List.of(task), pageable, 1);

        when(taskRepository.findByProjectIdWithFilters(
                projectId, "", TaskStatus.IN_PROGRESS, null, null, pageable
        )).thenReturn(expectedPage);

        // Act
        Page<Task> result = handler.executeByProjectWithFilters(
                projectId, null, "in_progress", null, null, pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        verify(taskRepository).findByProjectIdWithFilters(
                projectId, "", TaskStatus.IN_PROGRESS, null, null, pageable);
    }

    @Test
    void testExecuteByProjectWithFilters_WithPriorityFilter_ShouldReturnFilteredTasks() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);

        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setTitle("High Priority Task");
        task.setPriority(TaskPriority.HIGH);
        task.setActive(true);

        Page<Task> expectedPage = new PageImpl<>(List.of(task), pageable, 1);

        when(taskRepository.findByProjectIdWithFilters(
                projectId, "", null, TaskPriority.HIGH, null, pageable
        )).thenReturn(expectedPage);

        // Act
        Page<Task> result = handler.executeByProjectWithFilters(
                projectId, null, null, "high", null, pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getPriority()).isEqualTo(TaskPriority.HIGH);
    }

    @Test
    void testExecuteByProjectWithFilters_WithSearchFilter_ShouldReturnFilteredTasks() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);
        String searchTerm = "test";

        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setTitle("Test Task");
        task.setActive(true);

        Page<Task> expectedPage = new PageImpl<>(List.of(task), pageable, 1);

        when(taskRepository.findByProjectIdWithFilters(
                projectId, searchTerm, null, null, null, pageable
        )).thenReturn(expectedPage);

        // Act
        Page<Task> result = handler.executeByProjectWithFilters(
                projectId, searchTerm, null, null, null, pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(taskRepository).findByProjectIdWithFilters(
                projectId, searchTerm, null, null, null, pageable);
    }

    @Test
    void testExecuteByProjectWithFilters_WithAssigneeFilter_ShouldReturnFilteredTasks() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        UUID assigneeId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);

        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setTitle("Task for Assignee");
        task.setActive(true);

        Page<Task> expectedPage = new PageImpl<>(List.of(task), pageable, 1);

        when(taskRepository.findByProjectIdWithFilters(
                projectId, "", null, null, assigneeId, pageable
        )).thenReturn(expectedPage);

        // Act
        Page<Task> result = handler.executeByProjectWithFilters(
                projectId, null, null, null, assigneeId, pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(taskRepository).findByProjectIdWithFilters(
                projectId, "", null, null, assigneeId, pageable);
    }

    @Test
    void testExecuteByProjectWithFilters_WithInvalidStatus_ShouldIgnoreInvalidStatus() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);

        Page<Task> expectedPage = new PageImpl<>(List.of(), pageable, 0);

        when(taskRepository.findByProjectIdWithFilters(
                projectId, "", null, null, null, pageable
        )).thenReturn(expectedPage);

        // Act
        Page<Task> result = handler.executeByProjectWithFilters(
                projectId, null, "invalid_status", null, null, pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(0);
        verify(taskRepository).findByProjectIdWithFilters(
                projectId, "", null, null, null, pageable);
    }

    @Test
    void testExecuteByProjectWithFilters_WithInvalidPriority_ShouldIgnoreInvalidPriority() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);

        Page<Task> expectedPage = new PageImpl<>(List.of(), pageable, 0);

        when(taskRepository.findByProjectIdWithFilters(
                projectId, "", null, null, null, pageable
        )).thenReturn(expectedPage);

        // Act
        Page<Task> result = handler.executeByProjectWithFilters(
                projectId, null, null, "invalid_priority", null, pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(0);
    }
}

