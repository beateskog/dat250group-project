package no.hvl.dat250.feedapp.service.jpa;

import java.util.List;

import no.hvl.dat250.feedapp.model.jpa.Vote;


public interface VoteService {
    public Vote createVote(Vote vote);
    public Vote updateVote(Vote vote);
    public String deleteVote(Long voteId);
    public Vote getVote(Long voteId);
    public List<Vote> getAllVotes();
}