package pl.sonmiike.reportsservice;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import pl.sonmiike.reportsservice.report.generators.ReportCreator;

@RestController
@SpringBootApplication(scanBasePackages = "pl.sonmiike")
@RequiredArgsConstructor
public class ReportsServiceApplication implements CommandLineRunner {

    private final ReportCreator reportCreator;


    public static void main(String[] args) {
        SpringApplication.run(ReportsServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        reportCreator.executeMonthlyReports();
    }
}
