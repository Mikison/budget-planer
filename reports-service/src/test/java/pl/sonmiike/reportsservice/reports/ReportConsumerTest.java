package pl.sonmiike.reportsservice.reports;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sonmiike.reportsservice.report.ReportConsumer;
import pl.sonmiike.reportsservice.report.generators.ReportCreator;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class ReportConsumerTest {

    @Mock
    private ReportCreator reportCreator;

    @InjectMocks
    private ReportConsumer reportConsumer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testReceiveWeeklyReportMessage() {
        String message = "Generating Weekly Report for User: 12345";
        reportConsumer.receiveReportMessage(message);
        verify(reportCreator).generateWeeklyReport(12345L);
    }

    @Test
    public void testReceiveMonthlyReportMessage() {
        String message = "Generating Monthly Reports for User: 12345";
        reportConsumer.receiveReportMessage(message);
        verify(reportCreator).generateMonthlyReport(12345L);
    }

    @Test
    public void testReceiveInvalidMessage() {
        String message = "Invalid Message Content";
        reportConsumer.receiveReportMessage(message);
        verify(reportCreator, never()).generateWeeklyReport(anyLong());
        verify(reportCreator, never()).generateMonthlyReport(anyLong());
    }
}
