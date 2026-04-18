package com.example.task_manager_backend.features.auth.core;

public class UserBusinessException extends RuntimeException {
    public UserBusinessException(String message) {
        super(message);
    }
}

