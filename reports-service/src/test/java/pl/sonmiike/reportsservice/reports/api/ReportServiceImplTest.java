package pl.sonmiike.reportsservice.reports.api;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import pl.sonmiike.reportsservice.report.ReportExecutor;
import pl.sonmiike.reportsservice.report.api.ReportServiceImpl;
import pl.sonmiike.reportsservice.report.database.*;
import pl.sonmiike.reportsservice.user.UserReport;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportServiceImplTest {

    @Mock
    private ReportExecutor reportExecutor;

    @Mock
    private ReportEntityRepository reportEntityRepository;

    @Mock
    private ReportMapper reportMapper;

    @Mock
    private Resource resource;

    @InjectMocks
    private ReportServiceImpl reportServiceImpl;

    @Test
    void whenCallingWeeklyReport_thenInitiateWeeklyReportIsCalled() {
        Long userId = 1L;
        reportServiceImpl.callReportOnDemand(userId, ReportType.WEEKLY_REPORT);
        verify(reportExecutor).generateReportForUser(ReportType.WEEKLY_REPORT, userId);
    }

    @Test
    void whenCallingMonthlyReport_thenInitiateMonthlyReportIsCalled() {
        Long userId = 1L;
        reportServiceImpl.callReportOnDemand(userId, ReportType.MONTHLY_REPORT);
        verify(reportExecutor).generateReportForUser(ReportType.MONTHLY_REPORT, userId);
    }

    @Test
    void whenCallingCustomReport_thenInitiateCustomReportIsCalled() {
        Long userId = 1L;
        String startDate = LocalDate.now().toString();
        String endDate = LocalDate.now().plusDays(7).toString();
        reportServiceImpl.callCustomReportOnDemand(userId, startDate, endDate);
        verify(reportExecutor).initiateCustomReportGenerationForUser(userId, startDate, endDate);
    }


    @Test
    void whenFindingAllReports_thenAllReportsAreReturned() {
        List<ReportEntity> userReports = List.of(getReport());
        List<ReportDTO> expectedReports = List.of(getReportDTO());
        when(reportEntityRepository.findAll()).thenReturn(userReports);
        when(reportMapper.toDTO(any(ReportEntity.class))).thenReturn(expectedReports.get(0));

        List<ReportDTO> actualReports = reportServiceImpl.fetchAllReports();

        assertEquals(actualReports.size(), 1);
        assertEquals(actualReports.get(0), expectedReports.get(0));
        verify(reportMapper).toDTO(any(ReportEntity.class));
        verify(reportEntityRepository).findAll();

    }

    @Test
    void whenFindingUserReports_thenCorrectMethodCalled() {
        Long userId = 1L;
        List<ReportEntity> userReports = List.of(getReport());
        List<ReportDTO> expectedReports = List.of(getReportDTO());
        when(reportEntityRepository.findAllByUserUserId(userId)).thenReturn(userReports);
        when(reportMapper.toDTO(any(ReportEntity.class))).thenReturn(getReportDTO());

        List<ReportDTO> actualReports = reportServiceImpl.fetchUserReports(userId);

        assertEquals(actualReports.size(), 1);
        assertEquals(actualReports.get(0), expectedReports.get(0));
        verify(reportMapper).toDTO(any(ReportEntity.class));
        verify(reportEntityRepository).findAllByUserUserId(userId);
    }


    @Test
    void whenGettingPdfFileAndFileDoesNotExist_thenExceptionIsThrown() {
        Long userId = 1L;
        String fileName = "nonexistent";
        when(reportEntityRepository.findAllByUserUserId(userId)).thenReturn(Collections.emptyList());

        assertThrows(RuntimeException.class, () -> reportServiceImpl.fetchPdfFile(fileName, userId));
    }

    private ReportEntity getReport() {
        ReportEntity report = new ReportEntity();
        report.setFileName("testReport.pdf");
        report.setUser(new UserReport());
        report.setType(ReportType.WEEKLY_REPORT);
        report.setStartDate(LocalDate.now());
        report.setEndDate(LocalDate.now().plusDays(7));
        report.setGeneratedDate(LocalDate.now());

        return report;
    }

    private ReportDTO getReportDTO() {
        return ReportDTO.builder()
                .fileName("testReport.pdf")
                .type(ReportType.WEEKLY_REPORT)
                .startDate(LocalDate.now().toString())
                .endDate(LocalDate.now().plusDays(7).toString())
                .generatedDate(LocalDate.now().toString())
                .userId(1L)
                .build();
    }

}
