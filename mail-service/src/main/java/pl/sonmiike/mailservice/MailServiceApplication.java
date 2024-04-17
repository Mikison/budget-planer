package pl.sonmiike.mailservice;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.sonmiike.mailservice.mail.model.MailDTO;
import pl.sonmiike.mailservice.mail.model.Template;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication(scanBasePackages = "pl.sonmiike")
@EnableRabbit
@RequiredArgsConstructor
public class MailServiceApplication implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;

    public static void main(String[] args) {
        SpringApplication.run(MailServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        rabbitTemplate.convertAndSend("mail-exchange", "mail-routing-key", new MailDTO(Template.GREETING, "mati1241334@gmail.com", Template.GREETING.name(), Map.of()));
    }
}
