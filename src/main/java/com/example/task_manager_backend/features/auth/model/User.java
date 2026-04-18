package com.example.task_manager_backend.features.auth.model;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain placeholder for User.
 */
public class User {
    private UUID id;
    private String name;
    private String email;
    private String passwordHash;
    private String role;
    private boolean active;
    private Instant createdAt;

    // getters/setters omitted
}

