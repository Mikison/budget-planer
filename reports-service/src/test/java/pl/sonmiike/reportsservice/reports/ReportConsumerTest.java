package pl.sonmiike.reportsservice.reports;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sonmiike.reportsservice.report.ReportConsumer;
import pl.sonmiike.reportsservice.report.database.ReportType;
import pl.sonmiike.reportsservice.report.generators.ReportCreator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class ReportConsumerTest {

    @Mock
    private ReportCreator reportCreator;

    @InjectMocks
    private ReportConsumer reportConsumer;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testReceiveWeeklyReportMessage() {
        String message = "[>] Weekly Report: Generating for User: 12345";
        reportConsumer.receiveReportMessage(message);
        verify(reportCreator).generateReport(12345L, ReportType.WEEKLY_REPORT);
    }

    @Test
    void testReceiveMonthlyReportMessage() {
        String message = "[>] Monthly Report: Generating for User: 12345";
        reportConsumer.receiveReportMessage(message);
        verify(reportCreator).generateReport(12345L, ReportType.MONTHLY_REPORT);
    }

    @Test
    void testReceiveInvalidMessage() {
        String message = "Invalid Message Content";
        reportConsumer.receiveReportMessage(message);
        verify(reportCreator, never()).generateReport(anyLong(), any(ReportType.class));
    }
}
