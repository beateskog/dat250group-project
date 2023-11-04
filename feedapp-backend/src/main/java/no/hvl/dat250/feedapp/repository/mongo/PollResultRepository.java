package no.hvl.dat250.feedapp.repository.mongo;


import org.springframework.data.mongodb.repository.MongoRepository;

import no.hvl.dat250.feedapp.model.mongo.PollResult;

public interface PollResultRepository extends MongoRepository<PollResult, String> {
    
    /**
     * Find a poll result by its poll id
     * @param pollId the poll id of the poll result
     * @return the poll result
     */
    PollResult findByPollId(Long pollId);
}

