package com.example.task_manager_backend.features.tasks.core;

public class TaskBusinessException extends RuntimeException {
    public TaskBusinessException(String message) {
        super(message);
    }

    public TaskBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}

