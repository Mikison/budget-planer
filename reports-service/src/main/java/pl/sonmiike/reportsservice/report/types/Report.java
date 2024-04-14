package pl.sonmiike.reportsservice.report.types;


import pl.sonmiike.reportsservice.report.database.ReportType;
import pl.sonmiike.reportsservice.user.UserEntityReport;

import java.util.Map;



public interface Report {
    Map<String, Object> getReportData();
    UserEntityReport getUser();
    ReportType getReportType();
}
