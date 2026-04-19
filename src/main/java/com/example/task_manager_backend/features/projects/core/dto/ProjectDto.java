package com.example.task_manager_backend.features.projects.core.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ProjectDto(
    UUID id,
    String name,
    String description,
    UUID ownerId,
    String ownerName,
    List<ProjectMemberDto> members,
    Boolean active,
    Instant createdAt,
    Instant updatedAt
) {
    public record ProjectMemberDto(
        UUID id,
        UUID userId,
        String userName,
        String projectRole,
        Boolean active
    ) {
    }
}

