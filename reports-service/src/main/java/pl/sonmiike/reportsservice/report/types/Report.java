package pl.sonmiike.reportsservice.report.types;


import pl.sonmiike.reportsservice.report.repository.ReportType;
import pl.sonmiike.reportsservice.user.UserReport;

import java.util.Map;

public interface Report {
    Map<ReportDataKey, Object> getReportData();

    UserReport getUser();

    ReportType getReportType();
}
