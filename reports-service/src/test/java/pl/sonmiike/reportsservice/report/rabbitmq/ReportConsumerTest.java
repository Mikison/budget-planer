package pl.sonmiike.reportsservice.report.rabbitmq;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sonmiike.reportsservice.report.generators.ReportCreator;
import pl.sonmiike.reportsservice.report.generators.ReportRequest;
import pl.sonmiike.reportsservice.report.repository.ReportType;
import pl.sonmiike.reportsservice.report.util.MessageParser;

import static org.mockito.Mockito.*;

class ReportConsumerTest {

    @Mock
    private ReportCreator reportCreator;

    @Mock
    private MessageParser messageParser;

    @InjectMocks
    private ReportConsumer reportConsumer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testReceiveReportMessageWithValidMessage() {
        String message = "Some valid message";
        ReportRequest mockRequest = new ReportRequest(1L, ReportType.WEEKLY_REPORT, null);

        when(messageParser.parse(message)).thenReturn(mockRequest);

        reportConsumer.receiveReportMessage(message);

        verify(reportCreator).generateReport(mockRequest.getUserId(), mockRequest.getReportType(), mockRequest.getParameters());
    }

    @Test
    void testReceiveReportMessageWithInvalidMessage() {
        String message = "Invalid message";

        when(messageParser.parse(message)).thenReturn(null);

        reportConsumer.receiveReportMessage(message);

        verify(reportCreator, never()).generateReport(anyLong(), any(), any());
    }

}