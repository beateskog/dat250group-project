package no.hvl.dat250.feedapp.dto;


import java.io.Serializable;
import java.time.LocalDateTime;

import no.hvl.dat250.feedapp.model.Poll;
import no.hvl.dat250.feedapp.model.PollPrivacy;
import no.hvl.dat250.feedapp.model.Vote;

public class PollDTO implements Serializable {

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

    public static PollDTO pollToPollDTO(Poll poll) {

        PollDTO pollDTO = new PollDTO();
        pollDTO.id = poll.getId();
        pollDTO.pollUrl = poll.getPollURL();
        pollDTO.pollPin = poll.getPollPin();
        pollDTO.question = poll.getQuestion();
        pollDTO.startTime = poll.getStartTime();
        pollDTO.endTime = poll.getEndTime();
        pollDTO.pollPrivacy = poll.getPollPrivacy();
        pollDTO.pollOwner = poll.getAccount().getUsername();
        pollDTO.pollOwnerId = poll.getAccount().getId();
        pollDTO.totalVotes = poll.getVotes().size();
        pollDTO.yesVotes = 0;
        pollDTO.noVotes = 0;
        for (Vote vote : poll.getVotes()) {
            if (vote.isVote() == false) {
                pollDTO.noVotes++;
            } else {
                pollDTO.yesVotes++;
            }
        }

        return pollDTO;
    }

    public Long getId() {
        return id;
    }
    
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
