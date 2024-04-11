package pl.sonmiike.reportsservice.report.generators;

import pl.sonmiike.reportsservice.report.types.Report;

import java.util.List;
import java.util.Set;

public interface ReportPDFGenerator<T extends Report>  {

    Set<T> generatePDF();


}
