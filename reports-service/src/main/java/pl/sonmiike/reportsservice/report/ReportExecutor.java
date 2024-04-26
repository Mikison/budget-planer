package pl.sonmiike.reportsservice.report;


import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import pl.sonmiike.reportsservice.user.UserReport;
import pl.sonmiike.reportsservice.user.UserReportService;

import java.util.Set;

@Service
@Setter
@RequiredArgsConstructor
public class ReportExecutor {

    public static final String MONTHLY_REPORT_GENERATING_FOR_USER = "[>] Monthly Report: Generating for User: ";
    public static final String WEEKLY_REPORT_GENERATING_FOR_USER = "[>] Weekly Report: Generating for User: ";

    private final RabbitTemplate rabbitTemplate;
    private final UserReportService userReportService;

    @Value("${spring.rabbitmq.exchange}")
    private String topicExchangeName;

    @Value("${spring.rabbitmq.routing.key.weekly}")
    private String weeklyRoutingKey;

    @Value("${spring.rabbitmq.routing.key.monthly}")
    private String monthlyRoutingKey;

    @Scheduled(cron = "0 1 0 * * 1") // AT 00:01 ON MONDAY
    public void executeWeeklyReportGeneration() {
        initiateReportGenerationForAllUsers(weeklyRoutingKey, WEEKLY_REPORT_GENERATING_FOR_USER);
    }

    @Scheduled(cron = "0 1 0 1 * *") // AT 00:01 ON 1ST DAY OF MONTH
    public void executeMonthlyReportGeneration() {
        initiateReportGenerationForAllUsers(monthlyRoutingKey, MONTHLY_REPORT_GENERATING_FOR_USER);
    }

    private void initiateReportGenerationForAllUsers(String routingKey, String messagePrefix) {
        Set<UserReport> users = userReportService.getAllUsers();
        users.forEach(user -> sendMessageToGenerateReport(routingKey, messagePrefix, user.getUserId().toString()));
    }

    private void initiateReportGenerationForUser(String routingKey, String messagePrefix, String userId) {
        Assert.notNull(userId, "User ID must not be null");
        sendMessageToGenerateReport(routingKey, messagePrefix, userId);
    }

    public void initiateMonthlyReportGenerationForUser(Long userId) {
        initiateReportGenerationForUser(monthlyRoutingKey, MONTHLY_REPORT_GENERATING_FOR_USER, userId.toString());
    }

    public void initiateWeeklyReportGenerationForUser(Long userId) {
        initiateReportGenerationForUser(weeklyRoutingKey, WEEKLY_REPORT_GENERATING_FOR_USER, userId.toString());
    }

    private void sendMessageToGenerateReport(String routingKey, String messagePrefix, String userId) {
        Assert.hasText(routingKey, "Routing key must not be empty");
        Assert.hasText(messagePrefix, "Message prefix must not be empty");
        rabbitTemplate.convertAndSend(topicExchangeName, routingKey, messagePrefix + userId);
    }
}
