package no.hvl.dat250.feedapp.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;

import no.hvl.dat250.feedapp.model.Vote;
import no.hvl.dat250.feedapp.repository.VoteRepository;
import no.hvl.dat250.feedapp.service.VoteService;

@Service
public class VoteServiceImpl implements VoteService {
    // Can add business logic here

    // Getting instance of repository because repository is a layer which is talking to the database
    // Creating an instance of PollRepository and adding a constructor is enough to get the Poll repository
    // layer to interact with service layer.
    VoteRepository voteRepository;

    public VoteServiceImpl (VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @Override
    public Vote createVote(Vote vote) {
        return voteRepository.save(vote);
       
    }

    @Override
    public Vote updateVote(Vote vote) {
        return voteRepository.save(vote);
        
    }

    @Override
    public String deleteVote(Long voteId) {
        voteRepository.deleteById(voteId);
        return "Success";
    }

    @Override
    public Vote getVote(Long voteId) {
        return voteRepository.findById(voteId).get();
    }

    @Override
    public List<Vote> getAllVotes() {
        return voteRepository.findAll();
    }
}
