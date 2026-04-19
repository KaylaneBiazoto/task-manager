package com.example.task_manager_backend.features.reports.services;

import com.example.task_manager_backend.features.reports.core.dto.TaskReportDto;
import com.example.task_manager_backend.features.reports.usecases.GenerateProjectReportHandler;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReportFacade {
    private final GenerateProjectReportHandler generateProjectReportHandler;

    public ReportFacade(GenerateProjectReportHandler generateProjectReportHandler) {
        this.generateProjectReportHandler = generateProjectReportHandler;
    }

    public TaskReportDto generateProjectReport(UUID projectId) {
        return generateProjectReportHandler.execute(projectId);
    }
}

