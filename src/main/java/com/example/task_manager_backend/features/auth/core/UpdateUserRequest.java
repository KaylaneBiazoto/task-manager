package com.example.task_manager_backend.features.auth.core;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    private String username;

    @Email(message = "Email should be valid")
    private String email;

    private String password;

    private String role;
}

