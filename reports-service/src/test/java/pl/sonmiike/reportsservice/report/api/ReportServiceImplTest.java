package pl.sonmiike.reportsservice.report.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import pl.sonmiike.reportsservice.report.rabbitmq.ReportExecutor;
import pl.sonmiike.reportsservice.report.repository.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class ReportServiceImplTest {

    @Mock
    private ReportExecutor reportExecutor;

    @Mock
    private ReportEntityRepository reportEntityRepository;

    @Mock
    private ReportMapper reportMapper;

    @InjectMocks
    private ReportServiceImpl reportService;

    private final Long userId = 1L;
    private final String basePath = "path/to/reports";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reportService = new ReportServiceImpl(reportExecutor, reportEntityRepository, reportMapper);
        ReflectionTestUtils.setField(reportService, "basePath", basePath);
    }

    @Test
    void callReportOnDemand_DelegatesToReportExecutor() {
        ReportType type = ReportType.WEEKLY_REPORT;
        reportService.callReportOnDemand(userId, type);
        verify(reportExecutor).generateReportForUser(type, userId);
    }

    @Test
    void callCustomReportOnDemand_DelegatesToReportExecutor() {
        String startDate = "2021-01-01";
        String endDate = "2021-01-31";
        reportService.callCustomReportOnDemand(userId, startDate, endDate);
        verify(reportExecutor).callCustomReportOnDemand(userId, startDate, endDate);
    }

    @Test
    void fetchAllReports_ReturnsMappedReports() {
        List<ReportEntity> reports = new ArrayList<>();
        reports.add(new ReportEntity());
        List<ReportDTO> expectedDtos = new ArrayList<>();
        expectedDtos.add(new ReportDTO());

        when(reportEntityRepository.findAll()).thenReturn(reports);
        when(reportMapper.toDTO(any(ReportEntity.class))).thenReturn(expectedDtos.get(0));

        List<ReportDTO> dtos = reportService.fetchAllReports();

        assertEquals(expectedDtos, dtos);
        verify(reportMapper, times(reports.size())).toDTO(any(ReportEntity.class));
    }


    @Test
    void fetchPdfFile_ThrowsIfFileNotFound() {
        String fileName = "nonexistent";
        List<ReportDTO> reports = List.of();

        when(reportEntityRepository.findAllByUserUserId(userId)).thenReturn(new ArrayList<>());
        when(reportMapper.toDTO(any())).thenReturn(new ReportDTO());

        Exception exception = assertThrows(RuntimeException.class, () -> reportService.fetchPdfFile(fileName, userId));

        assertTrue(exception.getMessage().contains("Error: File not found"));
    }
}