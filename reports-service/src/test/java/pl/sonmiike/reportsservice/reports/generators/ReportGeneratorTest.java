package pl.sonmiike.reportsservice.reports.generators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sonmiike.reportsservice.report.generators.ReportGenerator;
import pl.sonmiike.reportsservice.report.types.WeeklyReport;
import pl.sonmiike.reportsservice.user.UserEntityService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReportGeneratorTest {

    @Mock
    private UserEntityService userEntityService;

    @InjectMocks
    private ReportGenerator<WeeklyReport> reportGenerator;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGeneratePdf_NullReport() {
        assertDoesNotThrow(() -> reportGenerator.generatePDF(null));
    }

    @Test
    void testGeneratePdf_NullUser() {
        WeeklyReport report = mock(WeeklyReport.class);
        when(report.getUser()).thenReturn(null);

        assertDoesNotThrow(() -> reportGenerator.generatePDF(report));
    }






}
