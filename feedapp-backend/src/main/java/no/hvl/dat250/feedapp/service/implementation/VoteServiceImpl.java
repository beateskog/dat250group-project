package no.hvl.dat250.feedapp.service.implementation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat250.feedapp.dto.VoteDTO;
import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.model.Poll;
import no.hvl.dat250.feedapp.model.Vote;
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

        Poll poll = pollRepository.findById(vote.getPollId()).get();
        newVote.setPoll(poll);
        Account account = accountRepository.findById(vote.getVoterId()).get();
        if (account != null) {
            newVote.setAccount(account);
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
        return voteRepository.findById(voteId).get();
    }

    @Override
    public List<Vote> getAllVotes() {
        return voteRepository.findAll();
    }
}
