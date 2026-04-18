package com.example.task_manager_backend.features.auth.api;

import com.example.task_manager_backend.core.dto.ApiResponse;
import com.example.task_manager_backend.features.auth.core.CreateUserRequest;
import com.example.task_manager_backend.features.auth.core.UpdateUserRequest;
import com.example.task_manager_backend.features.auth.core.UserDto;
import com.example.task_manager_backend.features.auth.services.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserDto>> createUser(
            @Valid @RequestBody CreateUserRequest request) {
        UserDto user = userService.createUser(request);
        ApiResponse<UserDto> response = new ApiResponse<>(true, "User created successfully", user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable Long userId) {
        UserDto user = userService.getUserById(userId);
        ApiResponse<UserDto> response = new ApiResponse<>(true, "User retrieved successfully", user);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserDto>>> listUsers(Pageable pageable) {
        Page<UserDto> users = userService.listUsers(pageable);
        ApiResponse<Page<UserDto>> response = new ApiResponse<>(true, "Users retrieved successfully", users);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserRequest request) {
        UserDto user = userService.updateUser(userId, request);
        ApiResponse<UserDto> response = new ApiResponse<>(true, "User updated successfully", user);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        ApiResponse<Void> response = new ApiResponse<>(true, "User deleted successfully", null);
        return ResponseEntity.ok(response);
    }
}

