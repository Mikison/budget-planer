package pl.sonmiike.reportsservice.report.rabbitmq;


import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.sonmiike.reportsservice.report.repository.ReportType;
import pl.sonmiike.reportsservice.user.UserReport;
import pl.sonmiike.reportsservice.user.UserReportService;

import java.util.Set;

@Service
@Setter
@RequiredArgsConstructor
public class ReportExecutor {

    private final UserReportService userReportService;
    private final ReportMessagingService reportMessagingService;

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
        String messagePrefix = String.format("[>] %s Report: Generating for User: ", reportType);
        Set<UserReport> users = userReportService.fetchAllUsers();
        users.forEach(user -> reportMessagingService.sendReportGenerationMessage(routingKey, messagePrefix, user.getUserId().toString()));
    }

    private String getRoutingKeyByType(ReportType reportType) {
        return switch (reportType) {
            case WEEKLY_REPORT -> weeklyRoutingKey;
            case MONTHLY_REPORT -> monthlyRoutingKey;
            case CUSTOM_DATE_REPORT -> customRoutingKey;
            default -> throw new IllegalArgumentException("Unsupported report type");
        };
    }


    public void callCustomReportOnDemand(Long userId, String startDate, String endDate) {
        initiateCustomReportGenerationForUser(userId, startDate, endDate);
    }

    public void generateReportForUser(ReportType reportType, Long userId) {
        String routingKey = getRoutingKeyByType(reportType);
        String messagePrefix = String.format("[>] %s Report: Generating for User: ", reportType);
        reportMessagingService.sendReportGenerationMessage(routingKey, messagePrefix, userId.toString());
    }

    private void initiateCustomReportGenerationForUser(Long userId, String startDate, String endDate) {
        reportMessagingService.initiateCustomReportGeneration(userId.toString(), startDate, endDate, this.customRoutingKey);
    }
}
