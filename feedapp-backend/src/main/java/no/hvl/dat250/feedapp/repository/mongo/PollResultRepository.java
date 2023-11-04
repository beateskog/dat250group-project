package no.hvl.dat250.feedapp.repository.mongo;


import org.springframework.data.mongodb.repository.MongoRepository;

import no.hvl.dat250.feedapp.model.mongo.PollResult;

public interface PollResultRepository extends MongoRepository<PollResult, String> {
    
    PollResult findByPollId(Long pollId);
}

