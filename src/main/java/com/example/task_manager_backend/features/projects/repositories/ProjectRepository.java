package com.example.task_manager_backend.features.projects.repositories;

import com.example.task_manager_backend.features.projects.domain.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    Optional<Project> findByIdAndActiveTrue(UUID id);
    Page<Project> findByActiveTrue(Pageable pageable);
    
    @Query("SELECT p FROM Project p WHERE p.active = true " +
           "AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Project> searchActiveProjects(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT p FROM Project p WHERE p.active = true " +
           "AND (:ownerId IS NULL OR p.ownerId = :ownerId) " +
           "AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Project> findActiveWithFilters(
            @Param("search") String search,
            @Param("ownerId") UUID ownerId,
            Pageable pageable);
}

