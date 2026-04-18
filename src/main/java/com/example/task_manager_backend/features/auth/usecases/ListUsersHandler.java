package com.example.task_manager_backend.features.auth.usecases;

import com.example.task_manager_backend.features.auth.domain.User;
import com.example.task_manager_backend.features.auth.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class ListUsersHandler {
    private final UserRepository userRepository;

    public ListUsersHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<User> execute(Pageable pageable) {
        return userRepository.findAllActive(pageable);
    }
    
    public Page<User> execute(String search, String role, Pageable pageable) {
        if ((search == null || search.isEmpty()) && (role == null || role.isEmpty())) {
            return userRepository.findAllActive(pageable);
        }
        
        String finalSearch = (search == null || search.isEmpty()) ? "" : search;
        String finalRole = (role == null || role.isEmpty()) ? null : role;
        
        if (finalRole == null) {
            return userRepository.searchActiveUsers(finalSearch, pageable);
        }
        
        return userRepository.findActiveWithFilters(finalSearch, finalRole, pageable);
    }
}

