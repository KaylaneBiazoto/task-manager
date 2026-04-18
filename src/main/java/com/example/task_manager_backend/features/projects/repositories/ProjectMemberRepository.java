package com.example.task_manager_backend.features.projects.repositories;

import com.example.task_manager_backend.features.projects.domain.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    List<ProjectMember> findByProjectIdAndActiveTrue(Long projectId);
}

