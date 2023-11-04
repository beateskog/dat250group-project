package no.hvl.dat250.feedapp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;


@Configuration
@Profile("!test")
public class RabbitMQConfig {

 
    @Bean
    public DirectExchange pollResultsExchange() {
        return new DirectExchange("pollResultsExchange");
    }

    @Bean
    public Queue pollResultsQueue() {
        return new Queue("pollResultsQueue");
    }

    @Bean
    public Binding bindQueueToExchange(Queue pollResultsQueue, DirectExchange pollResultsExchange) {
        return BindingBuilder.bind(pollResultsQueue).to(pollResultsExchange).with("");
    }
}
