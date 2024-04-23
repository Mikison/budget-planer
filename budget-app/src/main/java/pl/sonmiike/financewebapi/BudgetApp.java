package pl.sonmiike.financewebapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@SpringBootApplication
public class BudgetApp {

    public static void main(String[] args) {
        SpringApplication.run(BudgetApp.class, args);
    }

}
