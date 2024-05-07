package pl.sonmiike.reportsservice.report;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import pl.sonmiike.reportsservice.report.database.ReportType;


@Service
@RequiredArgsConstructor
public class RabbitMQReportMessagingService implements ReportMessagingService {


    private final RabbitTemplate rabbitTemplate;


    @Value("${spring.rabbitmq.exchange}")
    private String topicExchangeName;

    @Override
    public void sendReportGenerationMessage(String routingKey, String messagePrefix, String userId) {
        Assert.hasText(routingKey, "Routing key must not be empty");
        Assert.hasText(userId, "User ID must not be null");
        rabbitTemplate.convertAndSend(topicExchangeName, routingKey, messagePrefix + userId);
    }

    @Override
    public void initiateCustomReportGeneration(String userId, String startDate, String endDate, String routingKey) {
        String customMessage = String.format("[>] %s: Generating for User: %s Start Date: %s End Date: %s", ReportType.CUSTOM_DATE_REPORT.name() ,  userId, startDate, endDate);
        rabbitTemplate.convertAndSend(topicExchangeName, routingKey, customMessage );
    }
}
