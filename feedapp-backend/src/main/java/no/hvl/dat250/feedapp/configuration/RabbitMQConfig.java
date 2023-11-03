package no.hvl.dat250.feedapp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

@Configuration
@Profile("!test")
public class RabbitMQConfig {

    // @Bean
    // public Queue pollResultQueue() {
    //     return new Queue("pollResultQueue");
    // }
 
    // @Bean
    // public Jackson2JsonMessageConverter messageConverter() {
    //     return new Jackson2JsonMessageConverter();
    // }
}
