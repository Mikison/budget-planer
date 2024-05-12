package pl.sonmiike.reportsservice.report.generators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sonmiike.reportsservice.report.assemblers.ReportAssembler;
import pl.sonmiike.reportsservice.report.assemblers.ReportAssemblerFactory;
import pl.sonmiike.reportsservice.report.rabbitmq.ReportMailSender;
import pl.sonmiike.reportsservice.report.repository.ReportType;
import pl.sonmiike.reportsservice.report.types.Report;
import pl.sonmiike.reportsservice.report.types.WeeklyReport;
import pl.sonmiike.reportsservice.user.UserReport;
import pl.sonmiike.reportsservice.user.UserReportService;

import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;

class ReportCreatorTest {

    @Mock
    private ReportAssemblerFactory reportAssemblerFactory;
    @Mock
    private ReportGenerator<Report> reportGenerator;
    @Mock
    private ReportMailSender reportMailSender;
    @Mock
    private UserReportService userReportService;
    @Mock
    private ReportAssembler assembler;

    @InjectMocks
    private ReportCreator reportCreator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateReport_UserNotFound() {
        Long userId = 1L;
        when(userReportService.fetchUserById(userId)).thenReturn(Optional.empty());

        reportCreator.generateReport(userId, ReportType.WEEKLY_REPORT, Map.of());

        verifyNoInteractions(reportAssemblerFactory, reportGenerator, reportMailSender);
    }

    @Test
    void testGenerateReport_NotEnoughDataForReport() {
        Long userId = 1L;
        when(userReportService.fetchUserById(userId)).thenReturn(Optional.of(new UserReport(userId)));
        when(reportAssemblerFactory.getAssembler(ReportType.WEEKLY_REPORT)).thenReturn(assembler);
        when(assembler.createReport(any(), any())).thenReturn(null);

        reportCreator.generateReport(userId, ReportType.WEEKLY_REPORT, Map.of());

        verify(reportAssemblerFactory).getAssembler(ReportType.WEEKLY_REPORT);
        verifyNoMoreInteractions(reportGenerator, reportMailSender);
    }

    @Test
    void testGenerateReport_ErrorWhileGeneratingPdf() {
        Long userId = 1L;
        UserReport user = new UserReport(userId);
        Report report = new WeeklyReport();

        when(userReportService.fetchUserById(userId)).thenReturn(Optional.of(user));
        when(reportAssemblerFactory.getAssembler(ReportType.WEEKLY_REPORT)).thenReturn(assembler);
        when(assembler.createReport(user, Map.of())).thenReturn(report);
        when(reportGenerator.generatePdf(report)).thenReturn("");

        reportCreator.generateReport(userId, ReportType.WEEKLY_REPORT, Map.of());

        verify(reportGenerator).generatePdf(report);
        verifyNoInteractions(reportMailSender);
    }

    @Test
    void testGenerateReport_Successful() {
        Long userId = 1L;
        UserReport user = UserReport.builder()
                .userId(userId)
                .email("test@test.com")
                .build();

        Report report = new WeeklyReport();
        String fileName = "report.pdf";

        when(userReportService.fetchUserById(userId)).thenReturn(Optional.of(user));
        when(reportAssemblerFactory.getAssembler(ReportType.WEEKLY_REPORT)).thenReturn(assembler);
        when(assembler.createReport(user, Map.of())).thenReturn(report);
        when(reportGenerator.generatePdf(report)).thenReturn(fileName);

        reportCreator.generateReport(userId, ReportType.WEEKLY_REPORT, Map.of());

        verify(reportGenerator).generatePdf(report);
        verify(reportMailSender).sendReportMail(ReportType.WEEKLY_REPORT, fileName, user.getEmail());
    }
}