package no.hvl.dat250.feedapp.service.messaging;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessagingService {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendMessageToQueue(String queueName, Object message) {
        amqpTemplate.convertAndSend(queueName, message);
    }
}