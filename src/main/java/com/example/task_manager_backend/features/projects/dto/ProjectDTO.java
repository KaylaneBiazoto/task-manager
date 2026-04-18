package com.example.task_manager_backend.features.projects.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

public class ProjectDTO {
    public UUID id;
    public String name;
    public String description;
    public UUID ownerId;
    public ZonedDateTime createdAt;
    public ZonedDateTime updatedAt;
}

