package com.example.task_manager_backend.features.projects.model;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain model placeholder for Project. Real entity (JPA) will be created under
 * infrastructure.persistence.entity.
 */
public class Project {
    private UUID id;
    private String name;
    private String description;
    private UUID ownerId;
    private Instant createdAt;
    private Instant updatedAt;

    // getters/setters omitted for brevity
}

