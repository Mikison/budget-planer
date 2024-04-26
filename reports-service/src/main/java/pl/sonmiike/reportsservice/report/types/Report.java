package pl.sonmiike.reportsservice.report.types;


import pl.sonmiike.reportsservice.report.database.ReportType;
import pl.sonmiike.reportsservice.user.UserReport;

import java.util.Map;

public interface Report {
    Map<String, Object> getReportData();

    UserReport getUser();

    ReportType getReportType();
}
