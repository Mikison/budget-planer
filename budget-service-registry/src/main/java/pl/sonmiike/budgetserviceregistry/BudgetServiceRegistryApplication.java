package pl.sonmiike.budgetserviceregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


@EnableEurekaServer
@SpringBootApplication
public class BudgetServiceRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(BudgetServiceRegistryApplication.class, args);
    }

}
