package pl.sonmiike.reportsservice.report.rabbitmq;

import model.MailDTO;
import model.Template;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import pl.sonmiike.reportsservice.report.repository.ReportType;

import java.util.Map;

import static org.mockito.Mockito.verify;

class ReportMailSenderTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private ReportMailSender reportMailSender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendReportMail() {
        ReportType reportType = ReportType.MONTHLY_REPORT;
        String fileName = "monthly_report.pdf";
        String mail = "user@example.com";

        reportMailSender.sendReportMail(reportType, fileName, mail);

        verify(rabbitTemplate).convertAndSend(
                "mail-exchange",
                "mail-routing-key",
                new MailDTO(
                        Template.valueOf(reportType.name()),
                        mail,
                        reportType.name(),
                        Map.of(),
                        fileName
                )
        );
    }
}