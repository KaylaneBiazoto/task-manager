package com.example.task_manager_backend.features.tasks.usecases;

import com.example.task_manager_backend.features.tasks.core.exception.TaskNotFoundException;
import com.example.task_manager_backend.features.tasks.domain.Task;
import com.example.task_manager_backend.features.tasks.repositories.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetTaskByIdHandlerTest {

    @Mock
    private TaskRepository taskRepository;

    private GetTaskByIdHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GetTaskByIdHandler(taskRepository);
    }

    @Test
    void testExecute_WithValidTaskId_ShouldReturnTask() {
        // Arrange
        UUID taskId = UUID.randomUUID();
        Task expectedTask = new Task();
        expectedTask.setId(taskId);
        expectedTask.setTitle("Test Task");
        expectedTask.setActive(true);

        when(taskRepository.findByIdAndActiveTrue(taskId)).thenReturn(Optional.of(expectedTask));

        // Act
        Task result = handler.execute(taskId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(taskId);
        assertThat(result.getTitle()).isEqualTo("Test Task");
        assertThat(result.getActive()).isTrue();
        verify(taskRepository).findByIdAndActiveTrue(taskId);
    }

    @Test
    void testExecute_WithInvalidTaskId_ShouldThrowTaskNotFoundException() {
        // Arrange
        UUID invalidTaskId = UUID.randomUUID();
        when(taskRepository.findByIdAndActiveTrue(invalidTaskId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> handler.execute(invalidTaskId))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage("Task not found");

        verify(taskRepository).findByIdAndActiveTrue(invalidTaskId);
    }

    @Test
    void testExecute_WithInactiveTask_ShouldThrowTaskNotFoundException() {
        // Arrange
        UUID taskId = UUID.randomUUID();
        when(taskRepository.findByIdAndActiveTrue(taskId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> handler.execute(taskId))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage("Task not found");
    }
}

