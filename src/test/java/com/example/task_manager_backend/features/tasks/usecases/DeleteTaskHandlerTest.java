package com.example.task_manager_backend.features.tasks.usecases;

import com.example.task_manager_backend.features.tasks.core.exception.TaskBusinessException;
import com.example.task_manager_backend.features.tasks.domain.Task;
import com.example.task_manager_backend.features.auth.domain.User;
import com.example.task_manager_backend.features.tasks.repositories.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteTaskHandlerTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private DeleteTaskHandler deleteTaskHandler;

    @Test
    void testExecute_DeleteTaskWithAssignee_ShouldThrowException() {
        // Arrange
        UUID taskId = UUID.randomUUID();

        User assignee = new User();
        assignee.setId(UUID.randomUUID());
        assignee.setActive(true);

        Task task = new Task();
        task.setId(taskId);
        task.setActive(true);
        task.setAssignee(assignee);

        when(taskRepository.findByIdAndActiveTrue(taskId)).thenReturn(Optional.of(task));

        // Act & Assert
        assertThatThrownBy(() -> deleteTaskHandler.execute(taskId))
                .isInstanceOf(TaskBusinessException.class)
                .hasMessage("Cannot delete a task that has an assignee. Please remove the assignee first.");

        verify(taskRepository, never()).save(any());
    }

    @Test
    void testExecute_DeleteTaskWithoutAssignee_ShouldSucceed() {
        // Arrange
        UUID taskId = UUID.randomUUID();

        Task task = new Task();
        task.setId(taskId);
        task.setActive(true);
        task.setAssignee(null);

        when(taskRepository.findByIdAndActiveTrue(taskId)).thenReturn(Optional.of(task));

        // Act
        deleteTaskHandler.execute(taskId);

        // Assert
        assertThat(task.getActive()).isFalse();
        verify(taskRepository).save(task);
    }
}

