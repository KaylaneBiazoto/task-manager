package com.example.task_manager_backend.features.projects.core;

public class ProjectBusinessException extends RuntimeException {
    public ProjectBusinessException(String message) {
        super(message);
    }

    public ProjectBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}

