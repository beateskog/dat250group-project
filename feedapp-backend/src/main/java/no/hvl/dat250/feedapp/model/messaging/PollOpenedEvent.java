package no.hvl.dat250.feedapp.model.messaging;

public class PollOpenedEvent {
    
    private String pollId;
    private String needAccount;
    private String pollQuestion;
    private String startTime;

    public PollOpenedEvent() {
    }
    
    public PollOpenedEvent(String pollId, String needAccount,String pollQuestion, String startTime) {
        this.pollId = pollId;
        this.needAccount = needAccount;
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

    public String getNeedAccount() {
        return needAccount;
    }

    public void setNeedAccount(String needAccount) {
        this.needAccount = needAccount;
    }

}