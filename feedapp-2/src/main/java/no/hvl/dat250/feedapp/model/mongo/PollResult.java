package no.hvl.dat250.feedapp.model.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class PollResult {
    
    @Id
    private String id;

    private Long pollId;
    private int yesVotes; 
    private int noVotes;
    private int totalVotes;

    public PollResult() {
    }

    public PollResult(Long pollId, int yesVotes, int noVotes, int totalVotes) {
        this.pollId = pollId;
        this.yesVotes = yesVotes;
        this.noVotes = noVotes;
        this.totalVotes = totalVotes;
    }

    public String getId() {
        return id;
    }

    public  void setId(String id) {
        this.id = id;
    }

    public Long getPollId() {
        return pollId;
    }

    public void setPollId(Long id) {
        this.pollId = id;
    }

    public int getYesVotes() {
        return yesVotes;
    }

    public void setYesVotes(int yesVotes) {
        this.yesVotes = yesVotes;
    }

    public int getNoVotes() {
        return noVotes;
    }

    public void setNoVotes(int noVotes) {
        this.noVotes = noVotes;
    }

    public int getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(int total) {
        this.totalVotes = total;
    }
    
}

