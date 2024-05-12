package pl.sonmiike.reportsservice.report.generators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import pl.sonmiike.reportsservice.report.repository.ReportEntityRepository;
import pl.sonmiike.reportsservice.report.types.Report;
import pl.sonmiike.reportsservice.report.types.WeeklyReport;
import pl.sonmiike.reportsservice.report.util.PDFCreationHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReportGeneratorTest {

    @Mock
    private ReportEntityRepository reportEntityRepository;
    @Mock
    private PDFCreationHelper pdfCreationHelper;

    @InjectMocks
    private ReportGenerator<Report> reportGenerator;

    @Value("${reports.folder.root}")
    private String basePath;

    @TempDir
    Path tempDirectory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        basePath = tempDirectory.toString();
        reportGenerator.setBasePath(basePath);
    }

    @Test
    void init_CreatesDirectories() throws IOException {
        reportGenerator.init();
        assertTrue(Files.exists(tempDirectory));
    }

    @Test
    void generatePdf_CallsPdfCreationHelper() {
        Report report = new WeeklyReport(); // Assuming there is a concrete Report class
        String expectedPdfPath = "generated_report.pdf";

        when(pdfCreationHelper.generatePdf(report, basePath, reportEntityRepository)).thenReturn(expectedPdfPath);

        String pdfPath = reportGenerator.generatePdf(report);

        verify(pdfCreationHelper).generatePdf(report, basePath, reportEntityRepository);
        assertEquals(expectedPdfPath, pdfPath);
    }
}