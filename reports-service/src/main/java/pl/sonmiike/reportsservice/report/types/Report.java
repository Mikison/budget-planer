package pl.sonmiike.reportsservice.report.types;


import pl.sonmiike.reportsservice.user.UserEntityReport;

import java.util.Map;


public interface Report {
    Map<String, String> getReportData();
    UserEntityReport getUser();

}
