package pl.sonmiike.reportsservice.report;


import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.sonmiike.reportsservice.user.UserEntityReport;
import pl.sonmiike.reportsservice.user.UserEntityService;

import java.util.Set;

@Service
@Setter
@RequiredArgsConstructor
public class ReportExecutor {



    private final RabbitTemplate rabbitTemplate;
    private final UserEntityService userEntityService;

    @Value("${spring.rabbitmq.exchange}")
    private String topicExchangeName;

    @Value("${spring.rabbitmq.routing.key.weekly}")
    private String weeklyRoutingKey;

    @Value("${spring.rabbitmq.routing.key.monthly}")
    private String monthlyRoutingKey;

    @Scheduled(cron = "0 0 0 1 * *")
    public void executeWeeklyReportGeneration() {
        Set<UserEntityReport> users = userEntityService.getAllUsers();
        for (UserEntityReport user : users) {
            rabbitTemplate.convertAndSend(topicExchangeName, weeklyRoutingKey, "Generating Weekly Report for User: " + user.getUserId());
        }

    }

    @Scheduled(cron = "0 0 0 1 * *")
    public void executeMonthlyReportGeneration() {
        Set<UserEntityReport> users = userEntityService.getAllUsers();
        for (UserEntityReport user : users) {
            rabbitTemplate.convertAndSend(topicExchangeName, monthlyRoutingKey, "Generating Monthly Reports for User: " + user.getUserId());
        }
    }







}
