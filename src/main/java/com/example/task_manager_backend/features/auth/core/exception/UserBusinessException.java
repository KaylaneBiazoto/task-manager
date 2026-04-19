package com.example.task_manager_backend.features.auth.core.exception;

public class UserBusinessException extends RuntimeException {
    public UserBusinessException(String message) {
        super(message);
    }
}

