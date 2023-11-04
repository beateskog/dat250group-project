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

    /**
     * Handles a pollResultMessage
     * listens to the pollResultsQueue
     * Saves the poll result to the mongo database
     * @param pollDTO the poll result to handle
     */
    @RabbitListener(queues = "pollResultsQueue")
    public void handlePollResultMessage(PollDTO pollDTO) {
        pollResultService.savePollResult(pollDTO);
    }
}
