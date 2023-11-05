package no.hvl.dat250.feedapp.dto;

import no.hvl.dat250.feedapp.model.Role;
import no.hvl.dat250.feedapp.model.Vote;
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

    /**
     * Converts a vote to a voteDTO
     * @param vote the vote to convert
     * @return the voteDTO
     */
    public static VoteDTO voteToVoteDTO(Vote vote) {

        VoteDTO voteDTO = new VoteDTO();
        voteDTO.setId(vote.getId());
        if (vote.getAccount() == null) {
            voteDTO.setVoterId(null);
            voteDTO.setVoterRole(Role.ANONYMOUS_VOTER.toString());
        } else {
            voteDTO.setVoterId(vote.getAccount().getId());
            voteDTO.setVoterRole(vote.getAccount().getRole().toString());
        }
        voteDTO.setVote(vote.isVote());
        voteDTO.setVotingPlatform(vote.getPlatform());
        voteDTO.setPollId(vote.getPoll().getId());
        
        return voteDTO;
    }
   
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
