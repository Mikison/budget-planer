package pl.sonmiike.reportsservice.reports.generators;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sonmiike.reportsservice.report.generators.ReportGenerator;
import pl.sonmiike.reportsservice.report.types.Report;
import pl.sonmiike.reportsservice.report.types.WeeklyReport;
import pl.sonmiike.reportsservice.user.UserEntityService;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReportGeneratorTest {

    @Mock
    private UserEntityService userEntityService;  // Assuming this is a dependency

    @InjectMocks
    private ReportGenerator<WeeklyReport> reportGenerator;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGeneratePdf_NullReport() {
        assertDoesNotThrow(() -> reportGenerator.generatePDF(null));
        // Verify that no PDF operations are performed when report is null
    }

    @Test
    public void testGeneratePdf_NullUser() {
        WeeklyReport report = mock(WeeklyReport.class);
        when(report.getUser()).thenReturn(null);

        assertDoesNotThrow(() -> reportGenerator.generatePDF(report));
        // Check for handling of null user inside the PDF generation
    }

//    @Test
//    public void testPdfContent() throws Exception {
//        String basePath = "test-output";
//        WeeklyReport report = createSampleReport();
//        reportGenerator.generatePDF(report);
//
//        String outputPath = Paths.get(basePath, "generated_report.pdf").toString();
//
//        PdfDocument pdfDoc = new PdfDocument(new PdfReader(outputPath));
//        String pdfContent = PdfTextExtractor.getTextFromPage(pdfDoc.getPage(1));
//
//        assertTrue(pdfContent.contains("Expected text or data"));
//
//        pdfDoc.close();
//
//        new File(outputPath).delete();
//    }
//
//    private WeeklyReport createSampleReport() {
//        return WeeklyReport.builder()
//                .build();
//    }





}
