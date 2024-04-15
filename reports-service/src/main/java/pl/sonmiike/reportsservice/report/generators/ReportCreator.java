package pl.sonmiike.reportsservice.report.generators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sonmiike.reportsservice.report.generators.assemblers.CustomDateReportAssembler;
import pl.sonmiike.reportsservice.report.generators.assemblers.MonthlyReportAssembler;
import pl.sonmiike.reportsservice.report.generators.assemblers.WeeklyReportAssembler;
import pl.sonmiike.reportsservice.report.types.MonthlyReport;
import pl.sonmiike.reportsservice.report.types.WeeklyReport;
import pl.sonmiike.reportsservice.user.UserEntityReport;
import pl.sonmiike.reportsservice.user.UserEntityService;

import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class ReportCreator {


    private final ReportGeneratorFactory reportGeneratorFactory;
    private final UserEntityService userEntityService;

    private final WeeklyReportAssembler weeklyReportAssembler;
    private final MonthlyReportAssembler monthlyReportAssembler;
    private final CustomDateReportAssembler customDateIntervalReportGenerator;

    private static final Path PATH  =  Path.of("../reports");




    public void generateWeeklyReport(Long userId) {
        UserEntityReport user = userEntityService.getUserById(userId);
        WeeklyReport weeklyReport = weeklyReportAssembler.createWeeklyReport(user);
        if (weeklyReport == null) {
            System.out.println("Not Enought Data");
            return;
        }


        ReportGenerator<WeeklyReport> pdfGenerator = reportGeneratorFactory.createPDFGenerator();
        pdfGenerator.generatePDF(weeklyReport,PATH);
        System.out.println("Weekly Report Generated for user: " + userId);
    }

    public void generateMonthlyReport(Long userId) {
        UserEntityReport user = userEntityService.getUserById(userId);
        MonthlyReport monthlyReport = monthlyReportAssembler.createMonthlyReport(user);
        if (monthlyReport == null) {
            System.out.println("Not Enought Data");
            return;
        }
        ReportGenerator<MonthlyReport> pdfGenerator = reportGeneratorFactory.createPDFGenerator();
        pdfGenerator.generatePDF(monthlyReport, PATH);
        System.out.println("Monthly Report Generated for user: " + userId);
    }

    // TODO to implement
//    public void generateCustomDateIntervalReport(Long userId, LocalDate startDate, LocalDate endDate) {
//        WeeklyReport weeklyReports = customDateIntervalReportGenerator.createCustomDateIntervalReport(userId, startDate, endDate);
//        ReportGenerator<WeeklyReport> pdfGenerator = reportGeneratorFactory.createPDFGenerator(Set.of(weeklyReports));
//        pdfGenerator.generatePDF(PATH);
//    }

}
