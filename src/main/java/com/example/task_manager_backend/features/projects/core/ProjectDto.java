package com.example.task_manager_backend.features.projects.core;

import java.time.Instant;
import java.util.List;

public record ProjectDto(
    Long id,
    String name,
    String description,
    Long ownerId,
    String ownerName,
    List<ProjectMemberDto> members,
    Boolean active,
    Instant createdAt,
    Instant updatedAt
) {
    public record ProjectMemberDto(
        Long id,
        Long userId,
        String userName,
        String projectRole,
        Boolean active
    ) {
    }
}

