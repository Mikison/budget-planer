package pl.sonmiike.reportsservice.report.api;


import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.sonmiike.reportsservice.report.database.ReportEntity;
import pl.sonmiike.reportsservice.user.UserEntityService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {


    private final ReportService reportService;
    private final UserEntityService userEntityService;


    @GetMapping("/generate")
    @ResponseStatus(HttpStatus.CREATED)
    public void generateReport(@RequestParam String type, Authentication authentication) {
        Long userId = userEntityService.getUserId(authentication);
        if (type.equals("weekly")) {
            reportService.callOnDemandWeeklyReport(userId);
        } else if (type.equals("monthly")) {
            reportService.callOnDemandMonthlyReport(userId);
        } else {
            throw new RuntimeException("Report type not found");
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ReportEntity>> getAllReports() {
        return ResponseEntity.ok(reportService.findAllReports());
    }

    @GetMapping("/user")
    public ResponseEntity<List<ReportEntity> >getUserReports(Authentication authentication) {
        Long userId = userEntityService.getUserId(authentication);
        return ResponseEntity.ok(reportService.findUserReports(userId));
    }

    @GetMapping("/assets")
    public ResponseEntity<Resource> getUserReport(Authentication authentication, @RequestParam String name) {
        Long userId = userEntityService.getUserId(authentication);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(reportService.getPdfFile(name, userId));
    }
}
