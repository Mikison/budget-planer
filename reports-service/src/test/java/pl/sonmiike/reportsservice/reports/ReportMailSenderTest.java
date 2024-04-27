package pl.sonmiike.reportsservice.reports;

import model.MailDTO;
import model.Template;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import pl.sonmiike.reportsservice.report.ReportMailSender;
import pl.sonmiike.reportsservice.report.database.ReportType;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

public class ReportMailSenderTest {


    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private ReportMailSender reportMailSender;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void testSendReportMail() {
        String expectedRoutingKey = "mail-routing-key";
        String expectedTopicExchange = "mail-exchange";
        String expectedFileName = "report.pdf";
        String expectedMail = "user@example.com";
        ReportType reportType = ReportType.MONTHLY_REPORT;

        ArgumentCaptor<MailDTO> mailDTOArgumentCaptor = ArgumentCaptor.forClass(MailDTO.class);
        reportMailSender.sendReportMail(reportType, expectedFileName, expectedMail);

        verify(rabbitTemplate).convertAndSend(eq(expectedTopicExchange), eq(expectedRoutingKey), mailDTOArgumentCaptor.capture());
        MailDTO capturedMailDTO = mailDTOArgumentCaptor.getValue();

        assertNotNull(capturedMailDTO, "MailDTO should not be null");
        assertEquals(Template.valueOf(reportType.name()), capturedMailDTO.template(), "Template should match the report type");
        assertEquals(expectedMail, capturedMailDTO.recipient(), "Mail address should match");
        assertEquals(reportType.name(), capturedMailDTO.title(), "Subject should match the report type");
        assertEquals(expectedFileName, capturedMailDTO.fileName(), "File name should match");
        assertTrue(capturedMailDTO.templateProperties().isEmpty(), "Variables map should be empty");
    }

}
