package pl.sonmiike.reportsservice.report;


import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pl.sonmiike.reportsservice.report.generators.MessageParser;
import pl.sonmiike.reportsservice.report.generators.ReportCreator;
import pl.sonmiike.reportsservice.report.generators.ReportRequest;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ReportConsumer {

    private final ReportCreator reportCreator;
    private final MessageParser messageParser;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "${spring.rabbitmq.queue}", concurrency = "2-4")
    public void receiveReportMessage(String message) {
        ReportRequest request = messageParser.parse(message);
        if (request != null) {
            reportCreator.generateReport(request.getUserId(), request.getReportType(), request.getParameters());
        }
    }

    private Long extractUserId(String message) {
        Pattern pattern = Pattern.compile("User:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        }
        throw new IllegalArgumentException("No user ID found in the message.");
    }

    private LocalDate[] extractAndParseDates(String input) {
        String datePattern = "\\d{4}-\\d{2}-\\d{2}";
        Pattern pattern = Pattern.compile(datePattern);
        Matcher matcher = pattern.matcher(input);

        LocalDate[] dates = new LocalDate[2];
        int index = 0;

        while (matcher.find()) {
            if (index < 2) {
                String dateStr = matcher.group();
                dates[index++] = LocalDate.parse(dateStr);
            }
        }

        return dates;
    }
}
