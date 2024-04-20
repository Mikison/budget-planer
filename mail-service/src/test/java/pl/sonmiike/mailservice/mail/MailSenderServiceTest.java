package pl.sonmiike.mailservice.mail;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class MailSenderServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private MailSenderService mailSenderService;


    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void testSendMail() {
        MimeMessage mockMimeMessage = mock(MimeMessage.class);

        mailSenderService.sendMail(mockMimeMessage);

        verify(javaMailSender, times(1)).send(mockMimeMessage);
    }
}
