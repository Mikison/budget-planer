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
import pl.sonmiike.reportsservice.security.CustomUserDetails;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {


    private final ReportService reportService;


    @PostMapping("/generate")
    @ResponseStatus(HttpStatus.CREATED)
    public void generateReport(@RequestParam String type, Authentication authentication) {
        Long userId = getUserId(authentication);




        if (type.equals("weekly")) {
            reportService.callOnDemandWeeklyReport(userId);
        } else if (type.equals("monthly")) {
            reportService.callOnDemandMonthlyReport(userId);
        } else {
            throw new RuntimeException("Report type not found");
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReportDTO>> getAllReports() {
        return ResponseEntity.ok(reportService.fetchAllReports());
    }

    @GetMapping("/user")
    public ResponseEntity<List<ReportDTO>> getUserReports(Authentication authentication) {
        Long userId = getUserId(authentication);
        return ResponseEntity.ok(reportService.fetchUserReports(userId));
    }

    @GetMapping("/assets")
    public ResponseEntity<Resource> getPdfUserReport( @RequestParam String name, Authentication authentication) {
        Long userId = getUserId(authentication);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(reportService.fetchPdfFile(name, userId));
    }

    public Long getUserId(Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        return user.getUserId();
    }
}
