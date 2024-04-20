package pl.sonmiike.mailservice.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import model.MailDTO;
import model.Template;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class MailCreatorTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private MailCreator mailCreator;

    @Mock
    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        openMocks(this);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        ReflectionTestUtils.setField(mailCreator, "sender", "noreply@example.com");

    }

    @Test
    void testCreateMailToSend() throws MessagingException, MessagingException {
        MailDTO mail = new MailDTO(Template.GREETING, "recipient@example.com", "Subject", new HashMap<>());
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("Processed HTML content");

        MimeMessage result = mailCreator.createMailToSend(mail);

        verify(javaMailSender).createMimeMessage();
        assertNotNull(result);
        ArgumentCaptor<MimeMessageHelper> helperCaptor = ArgumentCaptor.forClass(MimeMessageHelper.class);
        verify(javaMailSender).createMimeMessage();
        verify(templateEngine).process(anyString(), any(Context.class));
    }

    @Test
    void testCreateMailToSendWithException() throws MessagingException {
        doThrow(new MessagingException()).when(mimeMessage).setContent(any(), anyString());

        MailDTO mail = new MailDTO(Template.GREETING, "recipient@example.com", "Subject", new HashMap<>());
        assertThrows(RuntimeException.class, () -> {
            mailCreator.createMailToSend(mail);
        });
    }


}
