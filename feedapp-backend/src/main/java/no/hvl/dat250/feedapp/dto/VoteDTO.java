package no.hvl.dat250.feedapp.dto;

import no.hvl.dat250.feedapp.model.VotingPlatform;

/**
 * Data transfer object for votes
 * Used to transfer votes between different layers of the application
 */ 
public class VoteDTO {

    private Long id;
    private Boolean vote;
    private String voterRole;
    private Long voterId;
    private Long pollId;
    private VotingPlatform platform;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setVote(Boolean vote) {
        this.vote = vote;
    }

    public Boolean getVote() {
        return vote;
    }

    public void setVoterRole(String voter) {
        this.voterRole = voter;
    }

    public String getVoterRole() {
        return voterRole;
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

    public void setVotingPlatform(VotingPlatform platform) {
        this.platform = platform;
    }

    public VotingPlatform getVotingPlatform() {
        return platform;
    }
    
}
