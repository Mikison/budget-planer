package pl.sonmiike.reportsservice.report.util;

import org.springframework.stereotype.Component;
import pl.sonmiike.reportsservice.report.generators.ReportRequest;
import pl.sonmiike.reportsservice.report.repository.ReportType;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MessageParser {

    public ReportRequest parse(String message) {
        if (message == null || message.isEmpty()) {
            return null;
        }

        Long userId = extractUserId(message);
        ReportType reportType = extractReportType(message);
        HashMap<String, Object> parameters = new HashMap<>();

        if (reportType == null) {
            return null;
        }

        if (reportType == ReportType.CUSTOM_DATE_REPORT) {
            LocalDate[] dates = extractAndParseDates(message);
            if (dates.length == 2) {
                parameters.put("startDate", dates[0]);
                parameters.put("endDate", dates[1]);
            }
        }

        return new ReportRequest(userId, reportType, parameters);
    }

    private Long extractUserId(String message) {
        Pattern pattern = Pattern.compile("User:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        }
        throw new IllegalArgumentException("No user ID found in the message.");
    }

    private ReportType extractReportType(String message) {
        for (ReportType type : ReportType.values()) {
            if (message.contains(type.name())) {
                return type;
            }
        }
        return null;
    }

    private LocalDate[] extractAndParseDates(String message) {
        Pattern datePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        Matcher matcher = datePattern.matcher(message);
        LocalDate[] dates = new LocalDate[2];
        int index = 0;

        while (matcher.find() && index < 2) {
            dates[index++] = LocalDate.parse(matcher.group());

        }

        if (index != 2) {
            throw new IllegalArgumentException("Invalid date information in the message.");
        }

        return dates;
    }
}