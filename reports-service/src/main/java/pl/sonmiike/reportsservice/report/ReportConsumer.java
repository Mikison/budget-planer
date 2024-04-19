package pl.sonmiike.reportsservice.report;


import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import pl.sonmiike.reportsservice.report.database.ReportType;
import pl.sonmiike.reportsservice.report.generators.ReportCreator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Pattern pattern = Pattern.compile("\\d+$");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            return Long.parseLong(matcher.group());
        }
        throw new IllegalArgumentException("No user ID found in the message.");
    }
}
