package com.example.task_manager_backend.features.projects.repositories;

import com.example.task_manager_backend.features.projects.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByIdAndActiveTrue(Long id);
}

