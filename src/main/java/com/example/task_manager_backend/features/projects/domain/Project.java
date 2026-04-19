package com.example.task_manager_backend.features.projects.domain;

import com.example.task_manager_backend.features.tasks.domain.Task;
import com.example.task_manager_backend.infrastructure.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true, exclude = {"tasks", "members"})
@ToString(exclude = {"tasks", "members"})
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "projects")
public class Project extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<Task> tasks;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<ProjectMember> members;

    @Column(nullable = false)
    private UUID ownerId;
}


