package pl.sonmiike.reportsservice.report.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.sonmiike.reportsservice.report.generators.ReportRequest;
import pl.sonmiike.reportsservice.report.repository.ReportType;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MessageParserTest {

    private MessageParser messageParser;

    @BeforeEach
    void setUp() {
        messageParser = new MessageParser();
    }

    @Test
    void parse_NullOrEmptyMessage_ReturnsNull() {
        assertNull(messageParser.parse(null));
        assertNull(messageParser.parse(""));
    }

    @Test
    void parse_MissingUserId_ThrowsException() {
        String message = "Report Type: DAILY_REPORT";
        assertThrows(IllegalArgumentException.class, () -> messageParser.parse(message));
    }

    @Test
    void parse_MissingReportType_ReturnsNull() {
        String message = "User: 123";
        assertNull(messageParser.parse(message));
    }

    @Test
    void parse_ValidMessage_ReturnsReportRequest() {
        String message = "User: 123 Report Type: WEEKLY_REPORT";
        ReportRequest result = messageParser.parse(message);
        assertNotNull(result);
        assertEquals(Long.valueOf(123), result.getUserId());
        assertEquals(ReportType.WEEKLY_REPORT, result.getReportType());
    }

    @Test
    void parse_CustomReportWithDates_ReturnsReportRequestWithDates() {
        String message = "User: 456 Report Type: CUSTOM_DATE_REPORT 2021-01-01 2021-01-31";
        ReportRequest result = messageParser.parse(message);
        assertNotNull(result);
        assertEquals(Long.valueOf(456), result.getUserId());
        assertEquals(ReportType.CUSTOM_DATE_REPORT, result.getReportType());
        assertEquals(LocalDate.of(2021, 1, 1), result.getParameters().get("startDate"));
        assertEquals(LocalDate.of(2021, 1, 31), result.getParameters().get("endDate"));
    }

    @Test
    void parse_InvalidDateInformation_ThrowsException() {
        String message = "User: 789 Report Type: CUSTOM_DATE_REPORT 2021-01-01";
        assertThrows(IllegalArgumentException.class, () -> messageParser.parse(message));
    }
}