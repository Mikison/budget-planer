package pl.sonmiike.reportsservice.report.rabbitmq;

public interface ReportMessagingService {
    void sendReportGenerationMessage(String routingKey, String messagePrefix, String userId);
    void initiateCustomReportGeneration(String userId, String startDate, String endDate, String routingKey);
}
