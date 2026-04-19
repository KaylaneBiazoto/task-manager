package com.example.task_manager_backend.features.reports.api;

import com.example.task_manager_backend.core.dto.ApiResponse;
import com.example.task_manager_backend.features.reports.core.dto.TaskReportDto;
import com.example.task_manager_backend.features.reports.services.ReportFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports", description = "API for generating task reports")
public class ReportController {
    private final ReportFacade reportFacade;

    public ReportController(ReportFacade reportFacade) {
        this.reportFacade = reportFacade;
    }

    @GetMapping("/projects/{projectId}")
    @Operation(summary = "Generate project task report with status and priority counts")
    public ResponseEntity<ApiResponse<TaskReportDto>> generateProjectReport(@PathVariable UUID projectId) {
        TaskReportDto report = reportFacade.generateProjectReport(projectId);
        ApiResponse<TaskReportDto> response = new ApiResponse<>(true, "Report generated successfully", report);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

