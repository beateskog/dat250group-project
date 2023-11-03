package no.hvl.dat250.feedapp.model.messaging;

public class PollClosedEvent {
    private String pollId;
    private String pollQuestion;
    private String endTime;
   
    public PollClosedEvent() {
    }

    public PollClosedEvent(String pollId, String pollQuestion, String endTime) {
        this.pollId = pollId;
        this.pollQuestion = pollQuestion;
        this.endTime = endTime;
    }

    public String getPollId() {
        return pollId;
    }

    public String getpollQuestion() {
        return pollQuestion;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public void setPollQuestion(String pollQuestion) {
        this.pollQuestion = pollQuestion;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

}