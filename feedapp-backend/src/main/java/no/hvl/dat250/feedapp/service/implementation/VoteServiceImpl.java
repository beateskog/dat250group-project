package no.hvl.dat250.feedapp.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import no.hvl.dat250.feedapp.dto.VoteDTO;
import no.hvl.dat250.feedapp.dto.iot.IoTResponse;
import no.hvl.dat250.feedapp.exception.ResourceNotFoundException;
import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.model.Poll;
import no.hvl.dat250.feedapp.model.Vote;
import no.hvl.dat250.feedapp.model.VotingPlatform;
import no.hvl.dat250.feedapp.repository.AccountRepository;
import no.hvl.dat250.feedapp.repository.PollRepository;
import no.hvl.dat250.feedapp.repository.VoteRepository;
import no.hvl.dat250.feedapp.service.VoteService;

@Service
public class VoteServiceImpl implements VoteService {
    // Can add business logic here

    // Getting instance of repository because repository is a layer which is talking to the database
    // Creating an instance of PollRepository and adding a constructor is enough to get the Poll repository
    // layer to interact with service layer.
    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Vote createVote(VoteDTO vote) {
        if (vote.getVote() == null) {
            throw new IllegalArgumentException("Vote cannot be null");
        }
        if (vote.getPollId() == null) {
            throw new IllegalArgumentException("PollId cannot be null");
        }

        if (vote.getVotingPlatform() == null) {
            throw new IllegalArgumentException("VotingPlatform cannot be null");
        }
        
        Vote newVote = new Vote();
        newVote.setVote(vote.getVote());
        voteRepository.save(newVote);

        Poll poll = pollRepository.findById(vote.getPollId()).get();
        newVote.setPoll(poll);
        if (vote.getVoterId() != null) {
            Account account = accountRepository.findById(vote.getVoterId()).get();
            if (account != null) {
                newVote.setAccount(account);
            }
        }
        
        newVote.setTimeOfVote(LocalDateTime.now());
        newVote.setPlatform(vote.getVotingPlatform());
        return voteRepository.save(newVote);
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
        Vote vote = voteRepository.findById(voteId) 
            .orElseThrow(() -> new ResourceNotFoundException("An account with the given ID: " + voteId + " does not exist."));
        return vote;
    }

    @Override
    public List<Vote> getAllVotes() {
        return voteRepository.findAll();
    }

    @Override
    public List<Vote> createIoTVote(IoTResponse response) {
        List<Vote> createdVotes = new ArrayList<>();
        int yesVotes = response.getYesVotes();
        int noVotes = response.getNoVotes();
        Long pollId = response.getPollId();
        for (int i = 0; i < yesVotes; i++) {
            Vote vote = new Vote();
            vote.setVote(true);
            vote.setPlatform(VotingPlatform.IoT);
            vote.setTimeOfVote(LocalDateTime.now());
            vote.setPoll(pollRepository.findById(pollId).get());
            voteRepository.save(vote);
            createdVotes.add(vote);
        }
        for (int i = 0; i < noVotes; i++) {
            Vote vote = new Vote();
            vote.setVote(false);
            vote.setPlatform(VotingPlatform.IoT);
            vote.setTimeOfVote(LocalDateTime.now());
            vote.setPoll(pollRepository.findById(pollId).get());
            voteRepository.save(vote);
            createdVotes.add(vote);
        }
        return createdVotes;
    }
}
