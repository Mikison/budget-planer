package pl.sonmiike.reportsservice.report.generators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sonmiike.reportsservice.report.database.ReportType;
import pl.sonmiike.reportsservice.report.generators.assemblers.CustomDateReportAssembler;
import pl.sonmiike.reportsservice.report.generators.assemblers.MonthlyReportAssembler;
import pl.sonmiike.reportsservice.report.generators.assemblers.WeeklyReportAssembler;
import pl.sonmiike.reportsservice.report.types.MonthlyReport;
import pl.sonmiike.reportsservice.report.types.Report;
import pl.sonmiike.reportsservice.report.types.WeeklyReport;
import pl.sonmiike.reportsservice.user.UserEntityReport;
import pl.sonmiike.reportsservice.user.UserEntityService;

@Component
@RequiredArgsConstructor
public class ReportCreator {

    private final UserEntityService userEntityService;
    private final WeeklyReportAssembler weeklyReportAssembler;
    private final MonthlyReportAssembler monthlyReportAssembler;
    private final CustomDateReportAssembler customDateIntervalReportGenerator;
    private final ReportGenerator<Report> reportGenerator; // Generic Report Generator

    public void generateReport(Long userId, ReportType reportType) {
        UserEntityReport user = userEntityService.getUserById(userId);
        if (user == null) {
            System.out.println("User not found");
            return;
        }

        Report report = generateReport(user, reportType);
        if (report == null) {
            System.out.println("Not Enough Data for " + reportType + " Report");
            return;
        }

        reportGenerator.generatePDF(report);
        System.out.println(reportType + " Report Generated for user: " + userId);
    }

    private Report generateReport(UserEntityReport user, ReportType reportType) {
        return switch (reportType) {
            case WEEKLY_REPORT -> weeklyReportAssembler.createWeeklyReport(user);
            case MONTHLY_REPORT -> monthlyReportAssembler.createMonthlyReport(user);
//           case "Custom" -> customDateIntervalReportGenerator.createCustomReport(user);
            default -> null;
        };
    }
}
