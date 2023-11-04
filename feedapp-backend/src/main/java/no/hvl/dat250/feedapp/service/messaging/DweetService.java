package no.hvl.dat250.feedapp.service.messaging;

import no.hvl.dat250.feedapp.model.messaging.PollOpenedEvent;
import no.hvl.dat250.feedapp.model.messaging.PollClosedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DweetService {

    //https://dweet.io/get/dweets/for/poll-opened-1
    //https://dweet.io/get/dweets/for/poll-closed-1
    private final String DWEET_BASE_URL = "https://dweet.io/dweet/for/";

    @Autowired
    private RestTemplate restTemplate;

    public void postPollOpenedEvent(PollOpenedEvent event) {
        String thingName = "poll-opened-" + event.getPollId(); 
        restTemplate.postForObject(DWEET_BASE_URL + thingName, event, String.class);
    }

    public void postPollClosedEvent(PollClosedEvent event) {
        String thingName = "poll-closed-" + event.getPollId(); 
        restTemplate.postForObject(DWEET_BASE_URL + thingName, event, String.class);
    }
}
