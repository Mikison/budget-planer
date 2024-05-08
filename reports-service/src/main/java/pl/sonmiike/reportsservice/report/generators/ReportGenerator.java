package pl.sonmiike.reportsservice.report.generators;

import com.itextpdf.kernel.colors.DeviceRgb;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.sonmiike.reportsservice.report.repository.ReportEntityRepository;
import pl.sonmiike.reportsservice.report.types.Report;
import pl.sonmiike.reportsservice.report.util.PDFCreationHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Setter
@Service
@RequiredArgsConstructor
public class ReportGenerator<T extends Report> {

    public static final DeviceRgb DARK_GREEN_COLOR = new DeviceRgb(50, 102, 71);
    public static final DeviceRgb DARK_RED_COLOR = new DeviceRgb(220, 20, 60);


    private final ReportEntityRepository reportEntityRepository;
    private final PDFCreationHelper pdfCreationHelper;


    @Value("${reports.folder.root}")
    private String basePath;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(basePath));
        } catch (IOException e) {
            throw new RuntimeException("Could not create report output directory", e);
        }
    }


    public String generatePdf(T report) {
        return pdfCreationHelper.generatePdf(report, basePath, reportEntityRepository);
    }
}
