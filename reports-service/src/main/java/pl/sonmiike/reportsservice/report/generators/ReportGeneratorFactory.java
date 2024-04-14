package pl.sonmiike.reportsservice.report.generators;

import org.springframework.stereotype.Component;
import pl.sonmiike.reportsservice.report.types.Report;

import java.util.Set;

@Component
public class ReportGeneratorFactory {

    public <T extends Report> ReportGenerator<T> createPDFGenerator(Set<T> reports) {
        return new ReportGenerator<>(reports);
    }
}