package no.hvl.dat250.feedapp.dto.iot;

public class IoTRequest {

    private Long pollId;
    private String question;

    public void setPollId(Long pollId) {
        this.pollId = pollId;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Long getPollId() {
        return pollId;
    }

    public String getQuestion() {
        return question;
    }

    
}
