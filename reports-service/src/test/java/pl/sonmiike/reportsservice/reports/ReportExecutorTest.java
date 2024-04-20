package pl.sonmiike.reportsservice.reports;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import pl.sonmiike.reportsservice.report.ReportExecutor;
import pl.sonmiike.reportsservice.user.UserEntityReport;
import pl.sonmiike.reportsservice.user.UserEntityService;

import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReportExecutorTest {


    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private UserEntityService userEntityService;

    @InjectMocks
    private ReportExecutor reportExecutor;

    private final String topicExchangeName = "testExchange";
    private final String weeklyRoutingKey = "weekly";
    private final String monthlyRoutingKey = "monthly";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reportExecutor = new ReportExecutor(rabbitTemplate, userEntityService);
        reportExecutor.setTopicExchangeName(topicExchangeName);
        reportExecutor.setWeeklyRoutingKey(weeklyRoutingKey);
        reportExecutor.setMonthlyRoutingKey(monthlyRoutingKey);

    }

    @Test
    void testExecuteWeeklyReportGeneration() {
        Set<UserEntityReport> users = Set.of(new UserEntityReport(1L), new UserEntityReport(2L));
        when(userEntityService.getAllUsers()).thenReturn(users);

        reportExecutor.executeWeeklyReportGeneration();

        for (UserEntityReport user : users) {
            verify(rabbitTemplate).convertAndSend(
                    topicExchangeName,
                    weeklyRoutingKey,
                    "[>] Weekly Report: Generating for User: " + user.getUserId()
            );
        }
    }

    @Test
    void testExecuteMonthlyReportGeneration() {
        Set<UserEntityReport> users = Set.of(new UserEntityReport(1L), new UserEntityReport(2L));
        when(userEntityService.getAllUsers()).thenReturn(users);

        reportExecutor.executeMonthlyReportGeneration();

        for (UserEntityReport user : users) {
            verify(rabbitTemplate).convertAndSend(
                    topicExchangeName,
                    monthlyRoutingKey,
                    "[>] Monthly Report: Generating for User: " + user.getUserId());
        }
    }
}



