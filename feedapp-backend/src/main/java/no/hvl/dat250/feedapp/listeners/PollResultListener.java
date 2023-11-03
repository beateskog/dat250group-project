package no.hvl.dat250.feedapp.listeners;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat250.feedapp.dto.PollDTO;
import no.hvl.dat250.feedapp.service.mongo.PollResultService;

@Service
public class PollResultListener {

    @Autowired
    private PollResultService pollResultService;

    // @RabbitListener(queues = "pollResultQueue")
    // public void handlePollResultMessage(PollDTO pollDTO) {
    //     pollResultService.savePollResult(pollDTO);
    // }
}
