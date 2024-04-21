package pl.sonmiike.mailservice.mail;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import model.MailDTO;
import model.Template;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;


@Component
@RequiredArgsConstructor
public class MailCreator {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String sender;

    public MimeMessage createMailToSend(MailDTO mail) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            File attachment = new File("../reports/" + mail.fileName());
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(sender);
            helper.setTo(mail.recipient());
            helper.setSubject(mail.title());
            helper.setText(getHtmlTemplate(mail), true);
            if (attachment.exists()) {
                helper.addAttachment(mail.fileName(), attachment);
            }

            return helper.getMimeMessage();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getHtmlTemplate(MailDTO mail) {
        Context context = new Context();
        context.setVariables(mail.templateProperties());
        return templateEngine.process(extractHtmlTemplateName(mail.template()), context);
    }

    private String extractHtmlTemplateName(Template template) {
        return template.toString().toLowerCase().replaceAll("_", "-") + "-template";
    }


}
