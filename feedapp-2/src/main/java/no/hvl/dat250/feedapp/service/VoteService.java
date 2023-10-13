package no.hvl.dat250.feedapp.service;

import java.util.List;

import no.hvl.dat250.feedapp.model.Vote;

public interface VoteService {
    public String createVote(Vote vote);
    public String updateVote(Vote vote);
    public String deleteVote(Long voteId);
    public Vote getVote(Long voteId);
    public List<Vote> getAllVotes();
}
