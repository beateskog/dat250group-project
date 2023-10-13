package no.hvl.dat250.feedapp.DTO;

import no.hvl.dat250.feedapp.model.VotingPlatform;

public class VoteDTO {

    public Long id;
    public Boolean vote;
    public String voter;
    public Long voterId;
    public Long pollId;
    public VotingPlatform votingPlatform;
    
}
