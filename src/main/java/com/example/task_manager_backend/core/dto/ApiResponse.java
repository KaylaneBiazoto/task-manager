package com.example.task_manager_backend.core.dto;

/**
 * Generic API response wrapper used across controllers.
 */
public class ApiResponse<T> {
    public boolean success;
    public T data;
    public String error;

    public ApiResponse() {}

    public static <T> ApiResponse<T> ok(T data) {
        ApiResponse<T> r = new ApiResponse<>();
        r.success = true;
        r.data = data;
        return r;
    }

    public static <T> ApiResponse<T> error(String message) {
        ApiResponse<T> r = new ApiResponse<>();
        r.success = false;
        r.error = message;
        return r;
    }
}

