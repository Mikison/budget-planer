package pl.sonmiike.reportsservice.report.generators;

import org.springframework.stereotype.Component;
import pl.sonmiike.reportsservice.report.types.Report;

import java.util.Set;

@Component
public class PDFReportGeneratorFactory {

    public <T extends Report> GenericPDFReportGenerator<T> createPDFGenerator(Set<T> reports) {
        return new GenericPDFReportGenerator<>(reports);
    }
}