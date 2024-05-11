package pl.sonmiike.reportsservice.report.rabbitmq;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import pl.sonmiike.reportsservice.report.repository.ReportType;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

class RabbitMQReportMessagingServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private RabbitMQReportMessagingService messagingService;

    @Value("${spring.rabbitmq.exchange}")
    private final String topicExchangeName = "test.exchange";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSendReportGenerationMessage() {
        String routingKey = "report.key";
        String messagePrefix = "Report for User: ";
        String userId = "123";

        messagingService.sendReportGenerationMessage(routingKey, messagePrefix, userId);

        verify(rabbitTemplate).convertAndSend(eq(topicExchangeName), eq(routingKey), eq(messagePrefix + userId));
    }

    @Test
    void testSendReportGenerationMessageWithEmptyValues() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            messagingService.sendReportGenerationMessage("", "Report for User: ", "123");
        });
        assertTrue(exception.getMessage().contains("Routing key must not be empty"));

        exception = assertThrows(IllegalArgumentException.class, () -> {
            messagingService.sendReportGenerationMessage("report.key", "Report for User: ", "");
        });
        assertTrue(exception.getMessage().contains("User ID must not be null"));
    }

    @Test
    void testInitiateCustomReportGeneration() {
        String userId = "456";
        String startDate = "2020-01-01";
        String endDate = "2020-01-31";
        String routingKey = "custom.report.key";
        String expectedMessage = String.format("[>] %s: Generating for User: %s Start Date: %s End Date: %s", ReportType.CUSTOM_DATE_REPORT.name(), userId, startDate, endDate);

        messagingService.initiateCustomReportGeneration(userId, startDate, endDate, routingKey);

        verify(rabbitTemplate).convertAndSend(eq(topicExchangeName), eq(routingKey), eq(expectedMessage));
    }
}