package pl.sonmiike.reportsservice.report.generators;

import pl.sonmiike.reportsservice.report.types.Report;

import java.nio.file.Path;

public interface ReportPDFGenerator<T extends Report>  {

    void generatePDF(T report ,Path path);


}
