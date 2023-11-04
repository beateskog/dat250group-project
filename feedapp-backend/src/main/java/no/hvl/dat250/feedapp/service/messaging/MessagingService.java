package no.hvl.dat250.feedapp.service.messaging;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessagingService {

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * Sends a message to the given queue
     * @param exchange the exchange to send the message to
     * @param routingKey the routing key to use
     * @param message the message to send
     */
    public void sendMessageToQueue(String exchange, String routingKey, Object message) {
        amqpTemplate.convertAndSend(exchange, routingKey, message);
    }
}