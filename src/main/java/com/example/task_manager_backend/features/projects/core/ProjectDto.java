package com.example.task_manager_backend.features.projects.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
    private Long id;
    private String name;
    private String description;
    private Long ownerId;
    private String ownerName;
    private List<ProjectMemberDto> members;
    private Boolean active;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectMemberDto {
        private Long id;
        private Long userId;
        private String userName;
        private String projectRole;
        private Boolean active;
    }
}

