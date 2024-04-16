package pl.sonmiike.mailservice.rabbitmq;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange}")
    private String topicExchange;

    @Value("${rabbitmq.queue}")
    private String mailQueue;



    @Bean
    Queue mailQueue() {
        return new Queue(mailQueue, true);
    }

    @Bean
    TopicExchange mailExchange() {
        return new TopicExchange(topicExchange);
    }

    @Bean
    Binding binding(Queue mailQueue, TopicExchange mailExchange) {
        return BindingBuilder.bind(mailQueue).to(mailExchange).with("mail.#");
    }
}
