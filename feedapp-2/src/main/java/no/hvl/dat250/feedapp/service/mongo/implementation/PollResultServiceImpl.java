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
    
    @Override
    public PollResult savePollResult(PollDTO pollDTO) {
        PollResult result = new PollResult();
        result.setPollId(pollDTO.id);
        result.setYesVotes(pollDTO.yesVotes);
        result.setNoVotes(pollDTO.noVotes);
        result.setTotalVotes(pollDTO.totalVotes);
        return pollResultRepository.save(result);
    }

}
