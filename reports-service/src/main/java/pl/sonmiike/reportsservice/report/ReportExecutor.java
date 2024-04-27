package pl.sonmiike.reportsservice.report;


import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import pl.sonmiike.reportsservice.report.database.ReportType;
import pl.sonmiike.reportsservice.user.UserReport;
import pl.sonmiike.reportsservice.user.UserReportService;

import java.util.Set;

@Service
@Setter
@RequiredArgsConstructor
public class ReportExecutor {

    public static final String MONTHLY_REPORT_GENERATING_FOR_USER = "[>] Monthly Report: Generating for User: ";
    public static final String WEEKLY_REPORT_GENERATING_FOR_USER = "[>] Weekly Report: Generating for User: ";
    public static final String CUSTOM_REPORT_GENERATING_FOR_USER = "[>] Custom Report: Generating for User: ";
    private static final String REPORT_GENERATING_FOR_USER = "[>] %s Report: Generating for User: ";
    private final RabbitTemplate rabbitTemplate;
    private final UserReportService userReportService;
    @Value("${spring.rabbitmq.exchange}")
    private String topicExchangeName;
    @Value("${spring.rabbitmq.routing.key.weekly}")
    private String weeklyRoutingKey;
    @Value("${spring.rabbitmq.routing.key.monthly}")
    private String monthlyRoutingKey;
    @Value("${spring.rabbitmq.routing.key.custom}")
    private String customRoutingKey;

    @Scheduled(cron = "0 1 0 * * 1") // AT 00:01 ON MONDAY
    public void executeWeeklyReportGeneration() {
        generateReportForAllUsers(ReportType.WEEKLY_REPORT);
    }

    @Scheduled(cron = "0 1 0 1 * *") // AT 00:01 ON 1ST DAY OF MONTH
    public void executeMonthlyReportGeneration() {
        generateReportForAllUsers(ReportType.MONTHLY_REPORT);
    }

    private void generateReportForAllUsers(ReportType reportType) {
        String routingKey = getRoutingKeyByType(reportType);
        String messagePrefix = String.format(REPORT_GENERATING_FOR_USER, reportType);
        Set<UserReport> users = userReportService.fetchAllUsers();
        users.forEach(user -> sendReportGenerationMessage(routingKey, messagePrefix, user.getUserId().toString()));
    }

    private String getRoutingKeyByType(ReportType reportType) {
        return switch (reportType) {
            case WEEKLY_REPORT -> weeklyRoutingKey;
            case MONTHLY_REPORT -> monthlyRoutingKey;
            case CUSTOM_DATE_REPORT -> customRoutingKey;
        };
    }

    public void generateReportForUser(ReportType reportType, Long userId) {
        String routingKey = getRoutingKeyByType(reportType);
        String messagePrefix = String.format(REPORT_GENERATING_FOR_USER, reportType);
        sendReportGenerationMessage(routingKey, messagePrefix, userId.toString());
    }

    private void sendReportGenerationMessage(String routingKey, String messagePrefix, String userId) {
        Assert.hasText(routingKey, "Routing key must not be empty");
        Assert.hasText(userId, "User ID must not be null");
        rabbitTemplate.convertAndSend(topicExchangeName, routingKey, messagePrefix + userId);
    }

    public void initiateCustomReportGenerationForUser(Long userId, String startDate, String endDate) {
        String customMessage = String.format(REPORT_GENERATING_FOR_USER, ReportType.CUSTOM_DATE_REPORT) + userId + " Start Date: " + startDate + " End Date: " + endDate;
        rabbitTemplate.convertAndSend(topicExchangeName, customRoutingKey, customMessage);
    }


}
