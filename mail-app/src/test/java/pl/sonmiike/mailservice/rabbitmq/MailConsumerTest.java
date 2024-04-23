package pl.sonmiike.mailservice.rabbitmq;

import jakarta.mail.internet.MimeMessage;
import model.MailDTO;
import model.Template;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sonmiike.mailservice.mail.MailCreator;
import pl.sonmiike.mailservice.mail.MailSenderService;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MailConsumerTest {

    @Mock
    private MailSenderService mailSenderService;

    @Mock
    private MailCreator mailCreator;

    @InjectMocks
    private MailConsumer mailConsumer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testConsume() {
        MailDTO mail = new MailDTO(Template.GREETING, "recipient@example.com", "Subject", new HashMap<>(), "");
        MimeMessage mockMimeMessage = mock(MimeMessage.class);

        when(mailCreator.createMailToSend(any(MailDTO.class))).thenReturn(mockMimeMessage);

        mailConsumer.consume(mail);

        verify(mailCreator).createMailToSend(mail);
        verify(mailSenderService).sendMail(mockMimeMessage);
    }

}
