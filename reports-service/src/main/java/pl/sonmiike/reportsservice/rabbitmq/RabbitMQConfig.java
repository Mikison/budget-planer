package pl.sonmiike.reportsservice.rabbitmq;


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
public class RabbitMQConfig {


    @Value("${rabbitmq.exchange}")
    private String topicExchangeName;

    @Value("${rabbitmq.queue}")
    private String reportsQueueName;



    @Bean
    Queue reportsQueue() {
        return new Queue(reportsQueueName, true);
    }

    @Bean
    TopicExchange reportsExchange() {
        return new TopicExchange(topicExchangeName);
    }


    @Bean
    Binding binding(Queue reportsQueue, TopicExchange reportsExchange) {
        return BindingBuilder.bind(reportsQueue).to(reportsExchange).with("reports.#");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}

