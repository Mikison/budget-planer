package pl.sonmiike.reportsservice.report.api;


import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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


    @PostMapping("/generate/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void generateReport(@RequestParam String type, @PathVariable Long userId) {

        if (type.equals("weekly")) {
            reportService.callOnDemandWeeklyReport(userId);
        } else if (type.equals("monthly")) {
            reportService.callOnDemandMonthlyReport(userId);
        } else {
            throw new RuntimeException("Report type not found");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReportDTO>> getAllReports(Authentication authentication) {
        System.out.println(getUserId(authentication));
        return ResponseEntity.ok(reportService.findAllReports());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReportDTO>> getUserReports(@PathVariable Long userId) {
        return ResponseEntity.ok(reportService.findUserReports(userId));
    }

    @GetMapping("/assets/{userId}")
    public ResponseEntity<Resource> getUserReport( @RequestParam String name, @PathVariable Long userId) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(reportService.getPdfFile(name, userId));
    }

    public Long getUserId(Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        return user.getUserId();
    }
}
