package pl.sonmiike.reportsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication(scanBasePackages = "pl.sonmiike")
//@ComponentScan("pl.sonmiike")
//@EntityScan("pl.sonmiike")
//@EnableJpaRepositories("pl.sonmiike")
public class ReportsServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(ReportsServiceApplication.class, args);
    }

}
