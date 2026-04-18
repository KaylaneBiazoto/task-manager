package com.example.task_manager_backend.features.tasks.repositories;

import com.example.task_manager_backend.features.tasks.domain.Task;
import com.example.task_manager_backend.features.tasks.domain.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByIdAndActiveTrue(Long id);

    List<Task> findByProjectIdAndActiveTrue(Long projectId);

    List<Task> findByAssigneeIdAndStatusAndActiveTrue(Long assigneeId, TaskStatus status);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.assignee.id = :assigneeId AND t.status = :status AND t.active = true")
    long countInProgressTasksByAssignee(@Param("assigneeId") Long assigneeId, @Param("status") TaskStatus status);

    List<Task> findByProjectIdAndStatusAndActiveTrue(Long projectId, TaskStatus status);
}

