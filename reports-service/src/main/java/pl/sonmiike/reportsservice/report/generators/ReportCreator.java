package pl.sonmiike.reportsservice.report.generators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sonmiike.reportsservice.report.ReportMailSender;
import pl.sonmiike.reportsservice.report.database.ReportType;
import pl.sonmiike.reportsservice.report.generators.assemblers.CustomDateReportAssembler;
import pl.sonmiike.reportsservice.report.generators.assemblers.MonthlyReportAssembler;
import pl.sonmiike.reportsservice.report.generators.assemblers.WeeklyReportAssembler;
import pl.sonmiike.reportsservice.report.types.Report;
import pl.sonmiike.reportsservice.user.UserReport;
import pl.sonmiike.reportsservice.user.UserReportService;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReportCreator {

    private final UserReportService userReportService;
    private final WeeklyReportAssembler weeklyReportAssembler;
    private final MonthlyReportAssembler monthlyReportAssembler;
    private final CustomDateReportAssembler customDateIntervalReportAssembler;
    private final ReportGenerator<Report> reportGenerator;
    private final ReportMailSender reportMailSender;

    private static LocalDate startDate;
    private static LocalDate endDate;

    public void generateReport(Long userId, ReportType reportType) {
        Optional<UserReport> userOpt = userReportService.fetchUserById(userId);
        if (userOpt.isEmpty()) {
            System.out.println("User not found");
            return;
        }
        UserReport user = userOpt.get();
        Report report = generateReport(user, reportType);
        if (report == null) {
            System.out.println("Not Enough Data for " + reportType + " Report");
            return;
        }

        String fileName = reportGenerator.generatePDF(report);
        System.out.println(reportType + " Report Generated for user: " + userId);
        reportMailSender.sendReportMail(reportType, fileName, user.getEmail());

    }

    private Report generateReport(UserReport user, ReportType reportType) {
        return switch (reportType) {
            case WEEKLY_REPORT -> weeklyReportAssembler.createWeeklyReport(user);
            case MONTHLY_REPORT -> monthlyReportAssembler.createMonthlyReport(user);
            case CUSTOM_DATE_REPORT -> customDateIntervalReportAssembler.createCustomDateReport(user, startDate, endDate);
        };
    }

    public void setCustomDates(LocalDate startDate, LocalDate endDate) {
        ReportCreator.startDate = startDate;
        ReportCreator.endDate = endDate;
    }
}
