package pl.sonmiike.mailservice.rabbitmq;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailQueueConfig {

    @Value("${spring.rabbitmq.exchange}")
    private String topicExchange;

    @Value("${spring.rabbitmq.queue}")
    private String mailQueue;

    @Value("${spring.rabbitmq.routing-key}")
    private String routingKey;



    @Bean
    Queue mailQueue() {
        return new Queue(mailQueue, true);
    }

    @Bean
    TopicExchange mailExchange() {
        return new TopicExchange(topicExchange);
    }

    @Bean
    Binding mailBinding(Queue mailQueue, TopicExchange mailExchange) {
        return BindingBuilder.bind(mailQueue).to(mailExchange).with(routingKey);
    }

    @Bean
    public MessageConverter mailJsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
