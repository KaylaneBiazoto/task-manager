package com.example.task_manager_backend.features.auth.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String role;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}

