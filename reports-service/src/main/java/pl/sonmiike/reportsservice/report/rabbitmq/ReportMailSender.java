package pl.sonmiike.reportsservice.report.rabbitmq;


import lombok.RequiredArgsConstructor;
import model.MailDTO;
import model.Template;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pl.sonmiike.reportsservice.report.repository.ReportType;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportMailSender {


    private final RabbitTemplate rabbitTemplate;

    public void sendReportMail(ReportType reportType, String fileName, String mail) {
        String routingKey = "mail-routing-key";
        String topicExchange = "mail-exchange";
        rabbitTemplate.convertAndSend(topicExchange, routingKey, new MailDTO(
                Template.valueOf(reportType.name()),
                mail,
                reportType.name(),
                Map.of(),
                fileName
        ));
    }
}
