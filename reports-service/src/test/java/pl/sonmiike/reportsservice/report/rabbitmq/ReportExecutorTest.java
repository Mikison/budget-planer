package pl.sonmiike.reportsservice.report.rabbitmq;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;
import pl.sonmiike.reportsservice.report.repository.ReportType;
import pl.sonmiike.reportsservice.user.UserReport;
import pl.sonmiike.reportsservice.user.UserReportService;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class ReportExecutorTest {

    @Mock
    private UserReportService userReportService;

    @Mock
    private ReportMessagingService reportMessagingService;

    @InjectMocks
    private ReportExecutor reportExecutor;


    @BeforeEach
    void setUp() {
        openMocks(this);
        ReflectionTestUtils.setField(reportExecutor, "weeklyRoutingKey", "reports.weekly");
        ReflectionTestUtils.setField(reportExecutor, "monthlyRoutingKey", "reports.monthly");
        ReflectionTestUtils.setField(reportExecutor, "customRoutingKey", "reports.custom");
    }

    @Test
    void testExecuteWeeklyReportGeneration() {
        Set<UserReport> users = new HashSet<>();
        users.add(new UserReport(1L));
        when(userReportService.fetchAllUsers()).thenReturn(users);

        reportExecutor.executeWeeklyReportGeneration();

        verify(reportMessagingService).sendReportGenerationMessage("reports.weekly", "[>] WEEKLY_REPORT Report: Generating for User: ", "1");
    }

    @Test
    void testExecuteMonthlyReportGeneration() {
        Set<UserReport> users = new HashSet<>();
        users.add(new UserReport(1L));
        when(userReportService.fetchAllUsers()).thenReturn(users);

        reportExecutor.executeMonthlyReportGeneration();

        verify(reportMessagingService).sendReportGenerationMessage("reports.monthly", "[>] MONTHLY_REPORT Report: Generating for User: ", "1");
    }

    @Test
    void testCallCustomReportOnDemand() {
        Long userId = 1L;
        String startDate = "2021-01-01";
        String endDate = "2021-01-31";

        reportExecutor.callCustomReportOnDemand(userId, startDate, endDate);

        verify(reportMessagingService).initiateCustomReportGeneration("1", startDate, endDate, "reports.custom");
    }

    @Test
    void testGenerateReportForUserWithWeeklyReport() {
        Long userId = 2L;
        reportExecutor.generateReportForUser(ReportType.WEEKLY_REPORT, userId);

        verify(reportMessagingService).sendReportGenerationMessage(
                "reports.weekly",
                "[>] WEEKLY_REPORT Report: Generating for User: ",
                userId.toString()
        );
    }

    @Test
    void testGenerateReportForUserWithMonthlyReport() {
        Long userId = 3L;
        reportExecutor.generateReportForUser(ReportType.MONTHLY_REPORT, userId);

        verify(reportMessagingService).sendReportGenerationMessage(
                "reports.monthly",
                "[>] MONTHLY_REPORT Report: Generating for User: ",
                userId.toString()
        );
    }

    @Test
    void testGenerateReportForUserWithCustomReport() {
        Long userId = 4L;
        reportExecutor.generateReportForUser(ReportType.CUSTOM_DATE_REPORT, userId);

        verify(reportMessagingService).sendReportGenerationMessage(
                "reports.custom",
                "[>] CUSTOM_DATE_REPORT Report: Generating for User: ",
                userId.toString()
        );
    }
}