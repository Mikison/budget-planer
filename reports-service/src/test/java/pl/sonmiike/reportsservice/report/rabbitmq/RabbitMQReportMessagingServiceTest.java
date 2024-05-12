package pl.sonmiike.reportsservice.report.rabbitmq;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import pl.sonmiike.reportsservice.report.repository.ReportType;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

class RabbitMQReportMessagingServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;
    private final String topicExchangeName = "test.exchange";

    private RabbitMQReportMessagingService service;

    @BeforeEach
    void setUp() {
        openMocks(this);
        service = new RabbitMQReportMessagingService(rabbitTemplate);
        ReflectionTestUtils.setField(service, "topicExchangeName", topicExchangeName);
    }

    @Test
    void testSendReportGenerationMessage() {
        String routingKey = "reportKey";
        String messagePrefix = "Report: ";
        String userId = "user123";

        service.sendReportGenerationMessage(routingKey, messagePrefix, userId);

        verify(rabbitTemplate).convertAndSend(topicExchangeName, routingKey, messagePrefix + userId);
    }

    @Test
    void testInitiateCustomReportGeneration() {
        String userId = "user123";
        String startDate = "2021-01-01";
        String endDate = "2021-01-31";
        String routingKey = "custom.report";

        service.initiateCustomReportGeneration(userId, startDate, endDate, routingKey);

        String expectedMessage = String.format("[>] %s: Generating for User: %s Start Date: %s End Date: %s", ReportType.CUSTOM_DATE_REPORT.name(), userId, startDate, endDate);

        verify(rabbitTemplate).convertAndSend(topicExchangeName, routingKey, expectedMessage);
    }
}