package pl.sonmiike.reportsservice.report.api;


import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.sonmiike.reportsservice.report.database.ReportDTO;
import pl.sonmiike.reportsservice.report.database.ReportType;
import pl.sonmiike.reportsservice.security.CustomUserDetails;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {


    private final ReportServiceImpl reportServiceImpl;


    @PostMapping("/generate")
    @ResponseStatus(HttpStatus.CREATED)
    public void generateReport(@RequestParam String type, Authentication authentication,
                               @RequestParam(required = false) String startDate,
                               @RequestParam(required = false) String endDate) {
        Long userId = getUserId(authentication);
        switch (type.toLowerCase()) {
            case "weekly" -> reportServiceImpl.callReportOnDemand(userId, ReportType.WEEKLY_REPORT);
            case "monthly" -> reportServiceImpl.callReportOnDemand(userId, ReportType.MONTHLY_REPORT);
            case "custom" -> reportServiceImpl.callCustomReportOnDemand(userId, startDate, endDate);
            default -> throw new RuntimeException("Report type not found");
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReportDTO>> getAllReports() {
        return ResponseEntity.ok(reportServiceImpl.fetchAllReports());
    }

    @GetMapping("/user")
    public ResponseEntity<List<ReportDTO>> getUserReports(Authentication authentication) {
        Long userId = getUserId(authentication);
        return ResponseEntity.ok(reportServiceImpl.fetchUserReports(userId));
    }

    @GetMapping("/assets")
    public ResponseEntity<Resource> getPdfUserReport(@RequestParam String name, Authentication authentication) {
        Long userId = getUserId(authentication);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(reportServiceImpl.fetchPdfFile(name, userId));
    }

    public Long getUserId(Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        return user.getUserId();
    }
}
