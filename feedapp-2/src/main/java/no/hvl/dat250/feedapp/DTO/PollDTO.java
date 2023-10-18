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
    public PollPrivacy pollPrivacy;
    public String pollOwner;
    public Long pollOwnerId;
    public int totalVotes;
    public int yesVotes;
    public int noVotes;
    
    public void setPollUrl(String pollUrl) {
        this.pollUrl = pollUrl;
    }

    public String getPollUrl() {
        return pollUrl;
    }

    public void setPollPin(int pollPin) {
        this.pollPin = pollPin;
    }

    public int getPollPin() {
        return pollPin;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setPollPrivacy(PollPrivacy privacy) {
        this.pollPrivacy = privacy;
    }

    public PollPrivacy getPollPrivacy() {
        return pollPrivacy;
    }

    public void setPollOwner(String pollOwner) {
        this.pollOwner = pollOwner;
    }

    public String getPollOwner() {
        return pollOwner;
    }

    public void setPollOwnerId(Long pollOwnerId) {
        this.pollOwnerId = pollOwnerId;
    }

    public Long getPollOwnerId() {
        return pollOwnerId;
    }

}
