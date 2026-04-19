package com.example.task_manager_backend.infrastructure.security;

import com.example.task_manager_backend.features.auth.domain.User;
import com.example.task_manager_backend.features.auth.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.Optional;

@Service
public class SecurityContextService {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(SecurityContextService.class);

    public SecurityContextService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public UUID getCurrentUserId() {
        Optional<User> user = getCurrentUser();
        return user.map(User::getId).orElse(null);
    }


    public Optional<User> getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || !authentication.isAuthenticated()) {
                logger.debug("No authentication found in security context");
                return Optional.empty();
            }
            
            Object principal = authentication.getPrincipal();
            
            // Se o principal é um User (da classe User implementando UserDetails)
            if (principal instanceof User) {
                User user = (User) principal;
                logger.debug("Found user in principal: {}", user.getEmail());
                // Buscar no banco para garantir que está ativo
                return userRepository.findByIdAndActiveTrue(user.getId());
            }
            
            // Se é UserDetails genérico, tentar extrair email
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                String identifier = userDetails.getUsername(); // pode ser email ou username
                logger.debug("UserDetails principal identifier: {}", identifier);
                
                // Tentar buscar por email primeiro
                Optional<User> userByEmail = userRepository.findByEmailAndActiveTrue(identifier);
                if (userByEmail.isPresent()) {
                    return userByEmail;
                }
                
                // Se não encontrou por email, tentar por username
                return userRepository.findByUsernameAndActiveTrue(identifier);
            }
            
            // Se principal é String (raro, mas possível)
            if (principal instanceof String) {
                String identifier = (String) principal;
                logger.debug("String principal: {}", identifier);
                
                Optional<User> userByEmail = userRepository.findByEmailAndActiveTrue(identifier);
                if (userByEmail.isPresent()) {
                    return userByEmail;
                }
                
                return userRepository.findByUsernameAndActiveTrue(identifier);
            }
            
            logger.warn("Unexpected principal type: {}", principal.getClass().getName());
        } catch (Exception e) {
            logger.error("Error getting current user from security context", e);
        }
        
        return Optional.empty();
    }


    public String getCurrentUserRole() {
        Optional<User> user = getCurrentUser();
        return user.map(User::getRole).orElse("MEMBER");
    }

}

