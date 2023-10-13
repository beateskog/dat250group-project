package no.hvl.dat250.feedapp.DTO;


import java.time.LocalDateTime;

import no.hvl.dat250.feedapp.model.PollPrivacy;

public class PollDTO {
    
    public Long id;
    public String pollUrl;
    public int pollPin;
    public String question;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public PollPrivacy privacy;
    public String pollOwner;
    public Long pollOwnerId;
    public int totalVotes;
    public int yesVotes;
    public int noVotes;
    
}
