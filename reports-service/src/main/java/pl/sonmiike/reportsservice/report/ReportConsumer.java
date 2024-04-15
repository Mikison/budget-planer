package pl.sonmiike.reportsservice.report;


import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import pl.sonmiike.reportsservice.report.generators.ReportCreator;

@Service
@RequiredArgsConstructor
public class ReportConsumer {

    private final ReportCreator reportCreator;


    @RabbitListener(queues = "${rabbitmq.queue}")
    public void receiveReportMessage(String message) {
        System.out.println("Received message: " + message);
        if (message.startsWith("Generating Weekly Report for User: ")) {
            Long userId = Long.parseLong(message.split(": ")[1]);
            reportCreator.generateWeeklyReport(userId);
            return;
        }

        if (message.startsWith("Generating Monthly Reports for User: ")) {
            Long userId = Long.parseLong(message.split(": ")[1]);
            reportCreator.generateMonthlyReport(userId);
        }

    }

}
