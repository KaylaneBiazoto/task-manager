package com.example.task_manager_backend.features.auth.repositories;

import com.example.task_manager_backend.features.auth.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByIdAndActiveTrue(UUID id);
    Optional<User> findByEmailAndActiveTrue(String email);
    Optional<User> findByUsernameAndActiveTrue(String username);
    
    @Query("SELECT u FROM User u WHERE u.active = true")
    Page<User> findAllActive(Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.active = true " +
           "AND (LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<User> searchActiveUsers(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.active = true " +
           "AND (:role IS NULL OR u.role = :role) " +
           "AND (LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<User> findActiveWithFilters(
            @Param("search") String search,
            @Param("role") String role,
            Pageable pageable);
}

