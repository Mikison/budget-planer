package pl.sonmiike.reportsservice.report.assemblers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sonmiike.reportsservice.report.repository.ReportType;

import static org.junit.jupiter.api.Assertions.*;

class ReportAssemblerFactoryTest {

    @Mock
    private WeeklyReportAssembler weeklyReportAssembler;
    @Mock
    private MonthlyReportAssembler monthlyReportAssembler;
    @Mock
    private CustomDateReportAssembler customDateReportAssembler;

    private ReportAssemblerFactory factory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        factory = new ReportAssemblerFactory(weeklyReportAssembler, monthlyReportAssembler, customDateReportAssembler);
    }

    @Test
    void testGetWeeklyReportAssembler() {
        ReportAssembler result = factory.getAssembler(ReportType.WEEKLY_REPORT);
        assertNotNull(result);
        assertInstanceOf(WeeklyReportAssembler.class, result);
    }

    @Test
    void testGetMonthlyReportAssembler() {
        ReportAssembler result = factory.getAssembler(ReportType.MONTHLY_REPORT);
        assertNotNull(result);
        assertInstanceOf(MonthlyReportAssembler.class, result);
    }

    @Test
    void testGetCustomDateReportAssembler() {
        ReportAssembler result = factory.getAssembler(ReportType.CUSTOM_DATE_REPORT);
        assertNotNull(result);
        assertInstanceOf(CustomDateReportAssembler.class, result);
    }

    @Test
    void testGetAssemblerWithInvalidType() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            factory.getAssembler(null);
        });
        assertEquals("No assembler available for type: null", exception.getMessage());
    }
}