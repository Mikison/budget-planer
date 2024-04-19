package pl.sonmiike.reportsservice.report.api;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.sonmiike.reportsservice.report.ReportExecutor;
import pl.sonmiike.reportsservice.report.database.ReportEntity;
import pl.sonmiike.reportsservice.report.database.ReportEntityRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportExecutor reportExecutor;
    private final ReportEntityRepository reportEntityRepository;

    public void callOnDemandWeeklyReport() {
        reportExecutor.executeWeeklyReportGeneration();
    }

    public void callOnDemandMonthlyReport() {
        reportExecutor.executeMonthlyReportGeneration();
    }

    public List<ReportEntity> findAllReports() {
        return reportEntityRepository.findAll();
    }

    public void openReportInPDF() {

    }



}
