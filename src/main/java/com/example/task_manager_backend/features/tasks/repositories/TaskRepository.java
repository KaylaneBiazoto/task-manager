package com.example.task_manager_backend.features.tasks.repositories;

import com.example.task_manager_backend.features.tasks.domain.Task;
import com.example.task_manager_backend.features.tasks.domain.TaskStatus;
import com.example.task_manager_backend.features.tasks.domain.TaskPriority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    Optional<Task> findByIdAndActiveTrue(UUID id);

    List<Task> findByProjectIdAndActiveTrue(UUID projectId);

    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId AND t.active = true")
    Page<Task> findByProjectIdActive(@Param("projectId") UUID projectId, Pageable pageable);

    List<Task> findByAssigneeIdAndStatusAndActiveTrue(UUID assigneeId, TaskStatus status);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.assignee.id = :assigneeId AND t.status = :status AND t.active = true")
    long countInProgressTasksByAssignee(@Param("assigneeId") UUID assigneeId, @Param("status") TaskStatus status);

    List<Task> findByProjectIdAndStatusAndActiveTrue(UUID projectId, TaskStatus status);
    
    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId AND t.active = true " +
           "AND (LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Task> searchByProjectId(@Param("projectId") UUID projectId, @Param("search") String search, Pageable pageable);
    
    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId AND t.active = true " +
           "AND (:status IS NULL OR t.status = :status) " +
           "AND (:priority IS NULL OR t.priority = :priority) " +
           "AND (:assigneeId IS NULL OR t.assignee.id = :assigneeId) " +
           "AND (LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Task> findByProjectIdWithFilters(
            @Param("projectId") UUID projectId,
            @Param("search") String search,
            @Param("status") TaskStatus status,
            @Param("priority") TaskPriority priority,
            @Param("assigneeId") UUID assigneeId,
            Pageable pageable);

    @Query("SELECT t.status, COUNT(t) FROM Task t WHERE t.project.id = :projectId AND t.active = true GROUP BY t.status")
    List<Object[]> countTasksByStatus(@Param("projectId") UUID projectId);

    @Query("SELECT t.priority, COUNT(t) FROM Task t WHERE t.project.id = :projectId AND t.active = true GROUP BY t.priority")
    List<Object[]> countTasksByPriority(@Param("projectId") UUID projectId);
}
