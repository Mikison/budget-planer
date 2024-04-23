package pl.sonmiike.reportsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "pl.sonmiike")
public class ReportsService {


    public static void main(String[] args) {
        SpringApplication.run(ReportsService.class, args);
    }

}
