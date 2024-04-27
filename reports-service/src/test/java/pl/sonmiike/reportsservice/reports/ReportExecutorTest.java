package pl.sonmiike.reportsservice.reports;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import pl.sonmiike.reportsservice.report.ReportExecutor;
import pl.sonmiike.reportsservice.report.database.ReportType;
import pl.sonmiike.reportsservice.user.UserReport;
import pl.sonmiike.reportsservice.user.UserReportService;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReportExecutorTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private UserReportService userReportService;

    @InjectMocks
    private ReportExecutor reportExecutor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(reportExecutor, "topicExchangeName", "test.exchange");
        ReflectionTestUtils.setField(reportExecutor, "weeklyRoutingKey", "weekly.key");
        ReflectionTestUtils.setField(reportExecutor, "monthlyRoutingKey", "monthly.key");
        ReflectionTestUtils.setField(reportExecutor, "customRoutingKey", "custom.key");
    }

    @Test
    void executeWeeklyReportGenerationTest() {
        UserReport user = new UserReport(1L); // Assuming UserReport has a constructor with userId
        Set<UserReport> users = Set.of(user);
        when(userReportService.getAllUsers()).thenReturn(users);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        reportExecutor.executeWeeklyReportGeneration();

        verify(rabbitTemplate).convertAndSend(eq("test.exchange"), eq("weekly.key"), captor.capture());
        assertTrue(captor.getValue().contains("WEEKLY_REPORT Report: Generating for User: " + user.getUserId()));
    }

    @Test
    void executeMonthlyReportGenerationTest() {
        UserReport user = new UserReport(2L);
        Set<UserReport> users = Set.of(user);
        when(userReportService.getAllUsers()).thenReturn(users);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        reportExecutor.executeMonthlyReportGeneration();

        verify(rabbitTemplate).convertAndSend(eq("test.exchange"), eq("monthly.key"), captor.capture());
        assertTrue(captor.getValue().contains("MONTHLY_REPORT Report: Generating for User: " + user.getUserId()));
    }

    @Test
    void generateReportForUserTest() {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        reportExecutor.generateReportForUser(ReportType.WEEKLY_REPORT, 3L);

        verify(rabbitTemplate).convertAndSend(eq("test.exchange"), eq("weekly.key"), captor.capture());
        assertTrue(captor.getValue().contains("WEEKLY_REPORT Report: Generating for User: 3"));
    }

    @Test
    void initiateCustomReportGenerationForUserTest() {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        reportExecutor.initiateCustomReportGenerationForUser(4L, "2021-01-01", "2021-01-31");

        verify(rabbitTemplate).convertAndSend(eq("test.exchange"), eq("custom.key"), captor.capture());
        assertTrue(captor.getValue().contains("CUSTOM_DATE_REPORT Report: Generating for User: 4"));
    }
}



