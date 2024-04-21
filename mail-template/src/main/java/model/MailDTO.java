package model;

import java.util.Map;

public record MailDTO(
        Template template,
        String recipient,
        String title,
        Map<String, Object> templateProperties,
        String fileName
) {
}
