package no.hvl.dat250.feedapp.service.mongo.implementation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat250.feedapp.dto.PollDTO;
import no.hvl.dat250.feedapp.model.mongo.PollResult;
import no.hvl.dat250.feedapp.repository.mongo.PollResultRepository;
import no.hvl.dat250.feedapp.service.mongo.PollResultService;

@Service
public class PollResultServiceImpl implements PollResultService {

    @Autowired
    private PollResultRepository pollResultRepository;
    
    public PollResult savePollResult(PollDTO pollDTO) {
        PollResult existingResults = pollResultRepository.findByPollId(pollDTO.id);

        PollResult result;

        if(existingResults != null) {
            result = existingResults;
            result.setQuestion(pollDTO.question);
            result.setYesVotes(pollDTO.yesVotes);
            result.setNoVotes(pollDTO.noVotes);
            result.setTotalVotes(pollDTO.totalVotes);
        } else {
            result = new PollResult();
            result.setPollId(pollDTO.id);
            result.setQuestion(pollDTO.question);
            result.setYesVotes(pollDTO.yesVotes);
            result.setNoVotes(pollDTO.noVotes);
            result.setTotalVotes(pollDTO.totalVotes);
        }
        
        return pollResultRepository.save(result);
    }

}
