package pl.sonmiike.reportsservice.report.rabbitmq;


import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import pl.sonmiike.reportsservice.report.generators.ReportCreator;
import pl.sonmiike.reportsservice.report.generators.ReportRequest;
import pl.sonmiike.reportsservice.report.util.MessageParser;

@Service
@RequiredArgsConstructor
public class ReportConsumer {

    private final ReportCreator reportCreator;
    private final MessageParser messageParser;

    @RabbitListener(queues = "${spring.rabbitmq.queue}", concurrency = "2-4")
    public void receiveReportMessage(String message) {
        ReportRequest request = messageParser.parse(message);
        if (request != null) {
            reportCreator.generateReport(request.getUserId(), request.getReportType(), request.getParameters());
        }
    }
}
