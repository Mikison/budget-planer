package pl.sonmiike.reportsservice.report.generators;

import org.springframework.stereotype.Component;
import pl.sonmiike.reportsservice.report.types.Report;

@Component
public class ReportGeneratorFactory {

    public <T extends Report> ReportGenerator<T> createPDFGenerator() {
        return new ReportGenerator<>();
    }
}