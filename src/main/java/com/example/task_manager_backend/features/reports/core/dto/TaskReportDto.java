package com.example.task_manager_backend.features.reports.core.dto;

import java.util.Map;
import java.util.UUID;

public record TaskReportDto(
    UUID projectId,
    Map<String, Long> byStatus,
    Map<String, Long> byPriority
) {
}

