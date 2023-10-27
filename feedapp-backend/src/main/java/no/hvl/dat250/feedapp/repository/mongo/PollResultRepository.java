package no.hvl.dat250.feedapp.repository.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import no.hvl.dat250.feedapp.model.mongo.PollResult;

public interface PollResultRepository extends MongoRepository<PollResult, String> {
    List<PollResult> findByPollId(String pollId);
}

