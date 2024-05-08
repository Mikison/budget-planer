package pl.sonmiike.reportsservice.report.assemblers;

import pl.sonmiike.reportsservice.report.types.Report;
import pl.sonmiike.reportsservice.user.UserReport;

import java.util.Map;

public interface ReportAssembler {
    Report createReport(UserReport user, Map<String, Object> parameters);
}
