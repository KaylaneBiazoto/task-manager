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

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByIdAndActiveTrue(Long id);

    List<Task> findByProjectIdAndActiveTrue(Long projectId);

    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId AND t.active = true")
    Page<Task> findByProjectIdActive(@Param("projectId") Long projectId, Pageable pageable);

    List<Task> findByAssigneeIdAndStatusAndActiveTrue(Long assigneeId, TaskStatus status);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.assignee.id = :assigneeId AND t.status = :status AND t.active = true")
    long countInProgressTasksByAssignee(@Param("assigneeId") Long assigneeId, @Param("status") TaskStatus status);

    List<Task> findByProjectIdAndStatusAndActiveTrue(Long projectId, TaskStatus status);
    
    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId AND t.active = true " +
           "AND (LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Task> searchByProjectId(@Param("projectId") Long projectId, @Param("search") String search, Pageable pageable);
    
    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId AND t.active = true " +
           "AND (:status IS NULL OR t.status = :status) " +
           "AND (:priority IS NULL OR t.priority = :priority) " +
           "AND (:assigneeId IS NULL OR t.assignee.id = :assigneeId) " +
           "AND (LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Task> findByProjectIdWithFilters(
            @Param("projectId") Long projectId,
            @Param("search") String search,
            @Param("status") TaskStatus status,
            @Param("priority") TaskPriority priority,
            @Param("assigneeId") Long assigneeId,
            Pageable pageable);
}

