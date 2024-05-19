package pl.sonmiike.mailservice;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableRabbit
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "pl.sonmiike")
public class MailApp {


    public static void main(String[] args) {
        SpringApplication.run(MailApp.class, args);
    }

}
