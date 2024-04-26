package pl.sonmiike.reportsservice.reports;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import pl.sonmiike.reportsservice.report.ReportExecutor;
import pl.sonmiike.reportsservice.user.UserReport;
import pl.sonmiike.reportsservice.user.UserReportService;

import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReportExecutorTest {


    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private UserReportService userReportService;

    @InjectMocks
    private ReportExecutor reportExecutor;

    private final String topicExchangeName = "testExchange";
    private final String weeklyRoutingKey = "weekly";
    private final String monthlyRoutingKey = "monthly";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reportExecutor = new ReportExecutor(rabbitTemplate, userReportService);
        reportExecutor.setTopicExchangeName(topicExchangeName);
        reportExecutor.setWeeklyRoutingKey(weeklyRoutingKey);
        reportExecutor.setMonthlyRoutingKey(monthlyRoutingKey);

    }

    @Test
    void testExecuteWeeklyReportGeneration() {
        Set<UserReport> users = Set.of(new UserReport(1L), new UserReport(2L));
        when(userReportService.getAllUsers()).thenReturn(users);

        reportExecutor.executeWeeklyReportGeneration();

        for (UserReport user : users) {
            verify(rabbitTemplate).convertAndSend(
                    topicExchangeName,
                    weeklyRoutingKey,
                    "[>] Weekly Report: Generating for User: " + user.getUserId()
            );
        }
    }

    @Test
    void testExecuteMonthlyReportGeneration() {
        Set<UserReport> users = Set.of(new UserReport(1L), new UserReport(2L));
        when(userReportService.getAllUsers()).thenReturn(users);

        reportExecutor.executeMonthlyReportGeneration();

        for (UserReport user : users) {
            verify(rabbitTemplate).convertAndSend(
                    topicExchangeName,
                    monthlyRoutingKey,
                    "[>] Monthly Report: Generating for User: " + user.getUserId());
        }
    }
}



