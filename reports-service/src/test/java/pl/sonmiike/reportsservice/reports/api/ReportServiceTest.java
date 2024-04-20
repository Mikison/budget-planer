package pl.sonmiike.reportsservice.reports.api;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import pl.sonmiike.reportsservice.report.ReportExecutor;
import pl.sonmiike.reportsservice.report.api.ReportService;
import pl.sonmiike.reportsservice.report.database.ReportEntity;
import pl.sonmiike.reportsservice.report.database.ReportEntityRepository;
import pl.sonmiike.reportsservice.report.database.ReportType;
import pl.sonmiike.reportsservice.user.UserEntityReport;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @Mock
    private ReportExecutor reportExecutor;

    @Mock
    private ReportEntityRepository reportEntityRepository;

    @Mock
    private Resource resource;

    @InjectMocks
    private ReportService reportService;

    @Test
    void whenCallingWeeklyReport_thenInitiateWeeklyReportIsCalled() {
        Long userId = 1L;
        reportService.callOnDemandWeeklyReport(userId);
        verify(reportExecutor).initiateWeeklyReportGenerationForUser(userId);
    }

    @Test
    void whenCallingMonthlyReport_thenInitiateMonthlyReportIsCalled() {
        Long userId = 1L;
        reportService.callOnDemandMonthlyReport(userId);
        verify(reportExecutor).initiateMonthlyReportGenerationForUser(userId);
    }


    @Test
    void whenFindingAllReports_thenAllReportsAreReturned() {
        List<ReportEntity> expectedReports = Collections.emptyList();
        when(reportEntityRepository.findAll()).thenReturn(expectedReports);
        List<ReportEntity> actualReports = reportService.findAllReports();
        assertSame(expectedReports, actualReports);
    }

    @Test
    void whenFindingUserReports_thenCorrectMethodCalled() {
        Long userId = 1L;
        List<ReportEntity> expectedReports = Collections.emptyList();
        when(reportEntityRepository.findAllByUserUserId(userId)).thenReturn(expectedReports);
        List<ReportEntity> actualReports = reportService.findUserReports(userId);
        assertSame(expectedReports, actualReports);
    }


    @Test
    void whenGettingPdfFileAndFileDoesNotExist_thenExceptionIsThrown() {
        Long userId = 1L;
        String fileName = "nonexistent";
        when(reportEntityRepository.findAllByUserUserId(userId)).thenReturn(Collections.emptyList());

        assertThrows(RuntimeException.class, () -> reportService.getPdfFile(fileName, userId));
    }

    private ReportEntity getReport() {
        ReportEntity report = new ReportEntity();
        report.setFileName("testReport.pdf");
        report.setUser(new UserEntityReport());
        report.setType(ReportType.WEEKLY_REPORT);
        report.setStartDate(LocalDate.now());
        report.setEndDate(LocalDate.now().plusDays(7));
        report.setGeneratedDate(LocalDate.now());

        return report;
    }
}
