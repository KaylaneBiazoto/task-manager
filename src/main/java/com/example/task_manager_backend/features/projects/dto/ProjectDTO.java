package com.example.task_manager_backend.features.projects.dto;

import java.time.Instant;
import java.util.UUID;

/**
 * Simple DTO for project REST input/output.
 */
public class ProjectDTO {
    public UUID id;
    public String name;
    public String description;
    public UUID ownerId;
    public Instant createdAt;
    public Instant updatedAt;
}

