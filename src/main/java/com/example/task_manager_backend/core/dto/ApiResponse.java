package com.example.task_manager_backend.core.dto;

public record ApiResponse<T>(
    boolean success,
    String message,
    T data
) {
}

