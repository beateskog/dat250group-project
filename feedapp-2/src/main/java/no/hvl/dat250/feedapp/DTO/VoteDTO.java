package no.hvl.dat250.feedapp.DTO;

import no.hvl.dat250.feedapp.model.VotingPlatform;

public class VoteDTO {

    public Long id;
    public Boolean vote;
    public String voter;
    public Long voterId;
    public Long pollId;
    public VotingPlatform platform;

    public void setVote(Boolean vote) {
        this.vote = vote;
    }

    public Boolean getVote() {
        return vote;
    }

    public void setVoter(String voter) {
        this.voter = voter;
    }

    public String getVoter() {
        return voter;
    }

    public void setVoterId(Long voterId) {
        this.voterId = voterId;
    }

    public Long getVoterId() {
        return voterId;
    }

    public void setPollId(Long pollId) {
        this.pollId = pollId;
    }

    public Long getPollId() {
        return pollId;
    }

    public void setPlatform(VotingPlatform platform) {
        this.platform = platform;
    }

    public VotingPlatform getVotingPlatform() {
        return platform;
    }
    
}
