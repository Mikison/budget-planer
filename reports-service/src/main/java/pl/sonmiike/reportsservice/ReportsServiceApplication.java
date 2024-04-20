package pl.sonmiike.reportsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication(scanBasePackages = "pl.sonmiike")
public class ReportsServiceApplication {




    public static void main(String[] args) {
        SpringApplication.run(ReportsServiceApplication.class, args);
    }

}
