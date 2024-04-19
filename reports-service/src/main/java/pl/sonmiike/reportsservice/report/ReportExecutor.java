package pl.sonmiike.reportsservice.report;


import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.sonmiike.reportsservice.user.UserEntityReport;
import pl.sonmiike.reportsservice.user.UserEntityService;

import java.util.Set;

@Service
@Setter
@RequiredArgsConstructor
public class ReportExecutor {

    public static final String MONTHLY_REPORT_GENERATING_FOR_USER = "[>] Monthly Report: Generating for User: ";
    public static final String WEEKLY_REPORT_GENERATING_FOR_USER = "[>] Weekly Report: Generating for User: ";

    private final RabbitTemplate rabbitTemplate;
    private final UserEntityService userEntityService;

    @Value("${spring.rabbitmq.exchange}")
    private String topicExchangeName;

    @Value("${spring.rabbitmq.routing.key.weekly}")
    private String weeklyRoutingKey;

    @Value("${spring.rabbitmq.routing.key.monthly}")
    private String monthlyRoutingKey;

    @Scheduled(cron = "0 1 0 * * 1") // AT 00:01 ON MONDAY
    public void executeWeeklyReportGeneration() {
        executeReportGeneration(weeklyRoutingKey, WEEKLY_REPORT_GENERATING_FOR_USER);
    }

    @Scheduled(cron = "0 1 0 1 * *") // AT 00:01 ON 1ST DAY OF MONTH
    public void executeMonthlyReportGeneration() {
        executeReportGeneration(monthlyRoutingKey, MONTHLY_REPORT_GENERATING_FOR_USER);
    }

    private void executeReportGeneration(String routingKey, String messagePrefix) {
        Set<UserEntityReport> users = userEntityService.getAllUsers();
        for (UserEntityReport user : users) {
            rabbitTemplate.convertAndSend(topicExchangeName, routingKey, messagePrefix + user.getUserId());
        }
    }
}
