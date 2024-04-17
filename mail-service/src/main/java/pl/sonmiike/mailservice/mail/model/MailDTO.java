package pl.sonmiike.mailservice.mail.model;

import java.io.Serializable;
import java.util.Map;

public record MailDTO(
        Template template,
        String recipient,
        String title,
        Map<String, Object> templateProperties
) {
}
