package pl.sonmiike.reportsservice.reports;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pl.sonmiike.reportsservice.report.ReportConsumer;
import pl.sonmiike.reportsservice.report.database.ReportType;
import pl.sonmiike.reportsservice.report.generators.ReportCreator;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

public class ReportConsumerTest {

    @Mock
    private ReportCreator reportCreator;

    @InjectMocks
    private ReportConsumer reportConsumer;

    @BeforeEach
    void setup() {
        openMocks(this);
    }

    @Test
    void testReceiveValidWeeklyReportMessage() {
        String message = "[>] WEEKLY_REPORT Report: Generating for User: 12345";
        reportConsumer.receiveReportMessage(message);
        verify(reportCreator).generateReport(12345L, ReportType.WEEKLY_REPORT);
    }

    @Test
    void testReceiveValidMonthlyReportMessage() {
        String message = "[>] MONTHLY_REPORT Report: Generating for User: 12345";
        reportConsumer.receiveReportMessage(message);
        verify(reportCreator).generateReport(12345L, ReportType.MONTHLY_REPORT);
    }

    @Test
    void testReceiveValidCustomDateReportMessage() {
        String message = "[>] CUSTOM_DATE_REPORT Report: Generating for User: 12345";
        reportConsumer.receiveReportMessage(message);
        verify(reportCreator).generateReport(12345L, ReportType.CUSTOM_DATE_REPORT);
    }

    @Test
    void testReceiveValidMessageWithNoUserId() {
        String message = "[>] WEEKLY_REPORT Report: Generating for User: ";
        assertThrows(IllegalArgumentException.class, () -> reportConsumer.receiveReportMessage(message));
        verify(reportCreator, never()).generateReport(anyLong(), any(ReportType.class));
    }

    @Test
    void testReceiveInvalidMessage() {
        String message = "Invalid Message Content";
        reportConsumer.receiveReportMessage(message);
        verify(reportCreator, never()).generateReport(anyLong(), any(ReportType.class));
    }
}
