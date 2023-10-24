package no.hvl.dat250.feedapp.service.mongo;

import no.hvl.dat250.feedapp.DTO.PollDTO;
import no.hvl.dat250.feedapp.model.mongo.PollResult;

public interface PollResultService {

    public PollResult savePollResult(PollDTO pollDTO);

}
