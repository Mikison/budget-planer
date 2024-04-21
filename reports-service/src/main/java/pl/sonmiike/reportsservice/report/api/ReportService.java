package pl.sonmiike.reportsservice.report.api;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import pl.sonmiike.reportsservice.report.ReportExecutor;
import pl.sonmiike.reportsservice.report.database.ReportDTO;
import pl.sonmiike.reportsservice.report.database.ReportEntityRepository;
import pl.sonmiike.reportsservice.report.database.ReportMapper;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    @Value("${reports.folder.root}")
    private String basePath;

    private final ReportExecutor reportExecutor;
    private final ReportEntityRepository reportEntityRepository;
    private final ReportMapper reportMapper;

    public void callOnDemandWeeklyReport(Long userId) {
        reportExecutor.initiateWeeklyReportGenerationForUser(userId);
    }

    public void callOnDemandMonthlyReport(Long userId) {
        reportExecutor.initiateMonthlyReportGenerationForUser(userId);
    }

    public List<ReportDTO> findAllReports() {
        return reportEntityRepository.findAll()
                .stream()
                .map(reportMapper::toDTO)
                .toList();
    }

    public List<ReportDTO> findUserReports(Long id) {
        return reportEntityRepository.findAllByUserUserId(id)
                .stream()
                .map(reportMapper::toDTO)
                .toList();
    }

    public Resource getPdfFile(String name, Long userId) {
        List<ReportDTO> userReports = findUserReports(userId);
        if (userReports.stream().noneMatch(report -> report.getFileName().contains(name))) {
            throw new RuntimeException("Error: File not found");
        }

        try {
            Resource file = new UrlResource(Paths.get(basePath, name + ".pdf").toUri());
            if (file.exists() || file.isReadable()) {
                return file;
            } else {
                throw new RuntimeException("Error: File not found");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }


}
