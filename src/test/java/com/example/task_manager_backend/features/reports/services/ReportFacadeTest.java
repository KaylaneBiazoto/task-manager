package com.example.task_manager_backend.features.reports.services;

import com.example.task_manager_backend.features.reports.core.dto.TaskReportDto;
import com.example.task_manager_backend.features.reports.usecases.GenerateProjectReportHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportFacadeTest {

    @Mock
    private GenerateProjectReportHandler generateProjectReportHandler;

    private ReportFacade reportFacade;

    @BeforeEach
    void setUp() {
        reportFacade = new ReportFacade(generateProjectReportHandler);
    }

    @Test
    void testGenerateProjectReport_ShouldReturnReport() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        Map<String, Long> byStatus = new HashMap<>();
        byStatus.put("TODO", 5L);
        byStatus.put("IN_PROGRESS", 3L);
        byStatus.put("DONE", 10L);

        Map<String, Long> byPriority = new HashMap<>();
        byPriority.put("LOW", 8L);
        byPriority.put("MEDIUM", 7L);
        byPriority.put("HIGH", 2L);
        byPriority.put("CRITICAL", 1L);

        TaskReportDto expectedReport = new TaskReportDto(projectId, byStatus, byPriority);

        when(generateProjectReportHandler.execute(projectId)).thenReturn(expectedReport);

        // Act
        TaskReportDto result = reportFacade.generateProjectReport(projectId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.projectId()).isEqualTo(projectId);
        assertThat(result.byStatus()).isEqualTo(byStatus);
        assertThat(result.byPriority()).isEqualTo(byPriority);

        verify(generateProjectReportHandler).execute(projectId);
    }
}

