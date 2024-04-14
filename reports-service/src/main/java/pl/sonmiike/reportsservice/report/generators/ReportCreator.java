package pl.sonmiike.reportsservice.report.generators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sonmiike.reportsservice.report.generators.Reports.CustomDateReportAssembler;
import pl.sonmiike.reportsservice.report.generators.Reports.MonthlyReportAssembler;
import pl.sonmiike.reportsservice.report.generators.Reports.WeeklyReportAssembler;
import pl.sonmiike.reportsservice.report.types.MonthlyReport;
import pl.sonmiike.reportsservice.report.types.WeeklyReport;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReportCreator {


    private final ReportGeneratorFactory reportGeneratorFactory;

    private final WeeklyReportAssembler weeklyReportAssembler;
    private final MonthlyReportAssembler monthlyReportAssembler;
    private final CustomDateReportAssembler customDateIntervalReportGenerator;

    private static final Path PATH  =  Path.of("../reports");




    public void executeWeeklyReports() {
        Set<WeeklyReport> weeklyReports = weeklyReportAssembler.createWeeklyReport();
        ReportGenerator<WeeklyReport> pdfGenerator = reportGeneratorFactory.createPDFGenerator(weeklyReports);
        pdfGenerator.generatePDF(PATH);
    }

    public void executeMonthlyReports() {
        Set<MonthlyReport> monthlyReports = monthlyReportAssembler.createMonthlyReport();
        ReportGenerator<MonthlyReport> pdfGenerator = reportGeneratorFactory.createPDFGenerator(monthlyReports);
        pdfGenerator.generatePDF(PATH);
    }


    public void executeCustomDateIntervalReport(Long userId, LocalDate startDate, LocalDate endDate) {
        WeeklyReport weeklyReports = customDateIntervalReportGenerator.createCustomDateIntervalReport(userId, startDate, endDate);
        ReportGenerator<WeeklyReport> pdfGenerator = reportGeneratorFactory.createPDFGenerator(Set.of(weeklyReports));
        pdfGenerator.generatePDF(PATH);
    }

}
