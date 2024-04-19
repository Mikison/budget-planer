package pl.sonmiike.reportsservice.report;


import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import pl.sonmiike.reportsservice.report.database.ReportType;
import pl.sonmiike.reportsservice.report.generators.ReportCreator;

@Service
@RequiredArgsConstructor
public class ReportConsumer {

    private final ReportCreator reportCreator;

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void receiveReportMessage(String message) {
        System.out.println("Received message: " + message);
        if (message.startsWith("[>] Weekly Report")) {
            reportCreator.generateReport(extractUserId(message), ReportType.WEEKLY_REPORT);
            return;
        }

        if (message.startsWith("[>] Monthly Report")) {
            reportCreator.generateReport(extractUserId(message), ReportType.MONTHLY_REPORT);
        }

    }

    private Long extractUserId(String message) {
        return Long.parseLong(message.split(": ")[1]);
    }
}
