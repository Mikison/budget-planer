package pl.sonmiike.reportsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication(scanBasePackages = "pl.sonmiike")
public class ReportsServiceApplication {

    @GetMapping
    public String hello(Authentication authentication) {
        return "Hello, " + authentication.getName() + "!";
    }

    public static void main(String[] args) {
        SpringApplication.run(ReportsServiceApplication.class, args);
    }

}
