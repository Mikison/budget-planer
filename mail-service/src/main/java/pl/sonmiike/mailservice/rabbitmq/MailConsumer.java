package pl.sonmiike.mailservice.rabbitmq;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import pl.sonmiike.mailservice.mail.MailCreator;
import pl.sonmiike.mailservice.mail.MailSenderService;
import pl.sonmiike.mailservice.mail.model.MailDTO;

@Component
@RequiredArgsConstructor
public class MailConsumer {

    private final MailSenderService mailSender;
    private final MailCreator mailCreator;

    @RabbitHandler
    @RabbitListener(queues = "${rabbitmq.queue}")
    public void consume(@Payload MailDTO mail) {
        System.out.println("Received mail: " + mail);
        MimeMessage mailToSend = mailCreator.createMailToSend(mail);
        mailSender.sendMail(mailToSend);

    }

}
