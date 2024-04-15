package pl.sonmiike.reportsservice;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.sonmiike.reportsservice.report.ReportExecutor;


@SpringBootApplication(scanBasePackages = "pl.sonmiike")
@RequiredArgsConstructor
@EnableScheduling
public class ReportsServiceApplication implements CommandLineRunner {

    private final ReportExecutor reportExecutor;


    public static void main(String[] args) {
        SpringApplication.run(ReportsServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        reportExecutor.executeMonthlyReportGeneration();

    }
}
