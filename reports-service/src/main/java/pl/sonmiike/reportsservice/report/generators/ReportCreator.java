package pl.sonmiike.reportsservice.report.generators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sonmiike.reportsservice.report.ReportMailSender;
import pl.sonmiike.reportsservice.report.database.ReportType;
import pl.sonmiike.reportsservice.report.generators.assemblers.ReportAssembler;
import pl.sonmiike.reportsservice.report.generators.assemblers.ReportAssemblerFactory;
import pl.sonmiike.reportsservice.report.types.Report;
import pl.sonmiike.reportsservice.user.UserReport;
import pl.sonmiike.reportsservice.user.UserReportService;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReportCreator {

    private final ReportAssemblerFactory reportAssemblerFactory;
    private final ReportGenerator<Report> reportGenerator;
    private final ReportMailSender reportMailSender;
    private final UserReportService userReportService;

    public void generateReport(Long userId, ReportType reportType, Map<String, Object> parameters) {
        Optional<UserReport> userOpt = userReportService.fetchUserById(userId);
        if (userOpt.isEmpty()) {
            System.out.println("User not found");
            return;
        }
        UserReport user = userOpt.get();

        ReportAssembler assembler = reportAssemblerFactory.getAssembler(reportType);
        Report report = assembler.createReport(user, parameters);

        if (report == null) {
            System.out.println("Not Enough Data for " + reportType + " Report");
            return;
        }

        String fileName = reportGenerator.generatePdf(report);
        if (fileName.isEmpty()) {
            System.out.println("Error while generating report");
            return;
        }
        System.out.println(reportType + " Report Generated for user: " + userId);

        reportMailSender.sendReportMail(reportType, fileName, user.getEmail());
    }
}
