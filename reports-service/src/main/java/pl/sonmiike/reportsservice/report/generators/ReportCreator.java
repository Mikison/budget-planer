package pl.sonmiike.reportsservice.report.generators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sonmiike.reportsservice.report.types.MonthlyReport;
import pl.sonmiike.reportsservice.report.types.WeeklyReport;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReportCreator {


    private final PDFReportGeneratorFactory pdfReportGeneratorFactory;

    private final WeeklyReportGenerator weeklyReportGenerator;
    private final MonthlyReportGenerator monthlyReportGenerator;
    private final CustomDateReportGenerator customDateIntervalReportGenerator;

    private static final Path PATH  =  Path.of("../reports");




    public void executeWeeklyReports() {
        Set<WeeklyReport> weeklyReports = weeklyReportGenerator.createWeeklyReport();
        GenericPDFReportGenerator<WeeklyReport> pdfGenerator = pdfReportGeneratorFactory.createPDFGenerator(weeklyReports);
        pdfGenerator.generatePDF(PATH);
    }

    public void executeMonthlyReports() {
        Set<MonthlyReport> monthlyReports = monthlyReportGenerator.createMonthlyReport();
        GenericPDFReportGenerator<MonthlyReport> pdfGenerator = pdfReportGeneratorFactory.createPDFGenerator(monthlyReports);
        pdfGenerator.generatePDF(PATH);
    }


    public void executeCustomDateIntervalReport(Long userId, LocalDate startDate, LocalDate endDate) {
        WeeklyReport weeklyReports = customDateIntervalReportGenerator.createCustomDateIntervalReport(userId, startDate, endDate);
        GenericPDFReportGenerator<WeeklyReport> pdfGenerator = pdfReportGeneratorFactory.createPDFGenerator(Set.of(weeklyReports));
        pdfGenerator.generatePDF(PATH);
    }

}
