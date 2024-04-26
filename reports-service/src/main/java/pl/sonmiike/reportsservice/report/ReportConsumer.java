package pl.sonmiike.reportsservice.report;


import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import pl.sonmiike.reportsservice.report.database.ReportType;
import pl.sonmiike.reportsservice.report.generators.ReportCreator;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ReportConsumer {

    private final ReportCreator reportCreator;

    @RabbitListener(queues = "${spring.rabbitmq.queue}", concurrency = "2-4")
    public void receiveReportMessage(String message) {
        System.out.println("Received message: " + message);
        if (message.startsWith("[>] " + ReportType.WEEKLY_REPORT.name())) {
            reportCreator.generateReport(extractUserId(message), ReportType.WEEKLY_REPORT);
            return;
        }

        if (message.startsWith("[>] " + ReportType.MONTHLY_REPORT.name())) {
            reportCreator.generateReport(extractUserId(message), ReportType.MONTHLY_REPORT);
            return;
        }

        if (message.startsWith("[>] "  + ReportType.CUSTOM_DATE_REPORT.name())) {
            LocalDate[] dates = extractAndParseDates(message);
            reportCreator.setCustomDates(dates[0], dates[1]);
            reportCreator.generateReport(extractUserId(message), ReportType.CUSTOM_DATE_REPORT);
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
