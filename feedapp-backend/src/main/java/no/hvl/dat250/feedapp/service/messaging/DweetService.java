package no.hvl.dat250.feedapp.service.messaging;

import no.hvl.dat250.feedapp.model.messaging.PollOpenedEvent;
import no.hvl.dat250.feedapp.model.messaging.PollClosedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DweetService {

    private final String DWEET_BASE_URL = "https://dweet.io/dweet/for/";

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Posts a poll opened event to dweet.io
     * @param event the event to post
     * Locate to https://dweet.io/get/latest/dweet/for/feedapp-poll-opened to see the event
     */
    public void postPollOpenedEvent(PollOpenedEvent event) {
        String thingName = "feedapp-poll-opened";
        restTemplate.postForObject(DWEET_BASE_URL + thingName, event, String.class);
    }

    /**
     * Posts a poll closed event to dweet.io
     * @param event the event to post
     * Locate to https://dweet.io/get/latest/dweet/for/feedapp-poll-closed to see the event
     */
    public void postPollClosedEvent(PollClosedEvent event) {
        String thingName = "feedapp-poll-closed";
        restTemplate.postForObject(DWEET_BASE_URL + thingName, event, String.class);
    }
}
