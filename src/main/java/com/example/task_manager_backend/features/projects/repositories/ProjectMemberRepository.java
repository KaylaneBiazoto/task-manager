package com.example.task_manager_backend.features.projects.repositories;

import com.example.task_manager_backend.features.projects.domain.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    List<ProjectMember> findByProjectIdAndActiveTrue(Long projectId);
    
    Optional<ProjectMember> findByIdAndActiveTrue(Long id);
}

