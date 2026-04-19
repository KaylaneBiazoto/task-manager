package com.example.task_manager_backend.features.tasks.usecases;

import com.example.task_manager_backend.features.tasks.domain.Task;
import com.example.task_manager_backend.features.tasks.repositories.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListTasksByProjectHandlerTest {

    @Mock
    private TaskRepository taskRepository;

    private ListTasksByProjectHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ListTasksByProjectHandler(taskRepository);
    }

    @Test
    void testExecute_WithValidProjectId_ShouldReturnTasks() {
        // Arrange
        UUID projectId = UUID.randomUUID();

        Task task1 = new Task();
        task1.setId(UUID.randomUUID());
        task1.setTitle("Task 1");
        task1.setActive(true);

        Task task2 = new Task();
        task2.setId(UUID.randomUUID());
        task2.setTitle("Task 2");
        task2.setActive(true);

        List<Task> expectedTasks = List.of(task1, task2);

        when(taskRepository.findByProjectIdAndActiveTrue(projectId)).thenReturn(expectedTasks);

        // Act
        List<Task> result = handler.execute(projectId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Task 1");
        assertThat(result.get(1).getTitle()).isEqualTo("Task 2");
        verify(taskRepository).findByProjectIdAndActiveTrue(projectId);
    }

    @Test
    void testExecute_WithValidProjectIdNoTasks_ShouldReturnEmptyList() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        List<Task> expectedTasks = List.of();

        when(taskRepository.findByProjectIdAndActiveTrue(projectId)).thenReturn(expectedTasks);

        // Act
        List<Task> result = handler.execute(projectId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(taskRepository).findByProjectIdAndActiveTrue(projectId);
    }

    @Test
    void testExecute_WithMultipleTasks_ShouldReturnAll() {
        // Arrange
        UUID projectId = UUID.randomUUID();

        List<Task> expectedTasks = new java.util.ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Task task = new Task();
            task.setId(UUID.randomUUID());
            task.setTitle("Task " + i);
            task.setActive(true);
            expectedTasks.add(task);
        }

        when(taskRepository.findByProjectIdAndActiveTrue(projectId)).thenReturn(expectedTasks);

        // Act
        List<Task> result = handler.execute(projectId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(5);
        for (int i = 0; i < 5; i++) {
            assertThat(result.get(i).getTitle()).isEqualTo("Task " + (i + 1));
        }
    }
}

