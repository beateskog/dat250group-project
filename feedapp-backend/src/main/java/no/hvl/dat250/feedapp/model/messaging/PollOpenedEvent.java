package no.hvl.dat250.feedapp.model.messaging;

public class PollOpenedEvent {
    
    private String pollId;
    private String pollQuestion;
    private String startTime;

    public PollOpenedEvent() {
    }
    
    public PollOpenedEvent(String pollId, String pollQuestion, String startTime) {
        this.pollId = pollId;
        this.pollQuestion = pollQuestion;
        this.startTime = startTime;
    }

    public String getPollId() {
        return pollId;
    }

    public String getpollQuestion() {
        return pollQuestion;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public void setPollQuestion(String pollQuestion) {
        this.pollQuestion = pollQuestion;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

}