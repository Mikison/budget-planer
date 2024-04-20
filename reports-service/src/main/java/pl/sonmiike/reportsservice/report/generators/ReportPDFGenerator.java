package pl.sonmiike.reportsservice.report.generators;

import pl.sonmiike.reportsservice.report.types.Report;

public interface ReportPDFGenerator<T extends Report> {

    void generatePDF(T report);
}
