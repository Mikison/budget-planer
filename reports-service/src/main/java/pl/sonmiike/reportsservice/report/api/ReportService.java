package pl.sonmiike.reportsservice.report.api;

import org.springframework.core.io.Resource;
import pl.sonmiike.reportsservice.report.repository.ReportDTO;
import pl.sonmiike.reportsservice.report.repository.ReportType;

import java.util.List;

public interface ReportService {


    void callReportOnDemand(Long userId, ReportType type);

    void callCustomReportOnDemand(Long userId, String startDate, String endDate);

    List<ReportDTO> fetchAllReports();

    List<ReportDTO> fetchUserReports(Long id);

    Resource fetchPdfFile(String name, Long userId);


}
