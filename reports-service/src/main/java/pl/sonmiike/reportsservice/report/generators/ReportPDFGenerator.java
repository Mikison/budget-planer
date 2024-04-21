package pl.sonmiike.reportsservice.report.generators;

import pl.sonmiike.reportsservice.report.types.Report;

public interface ReportPDFGenerator<T extends Report> {

    String generatePDF(T report);
}
