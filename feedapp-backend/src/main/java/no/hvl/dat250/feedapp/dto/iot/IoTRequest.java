package no.hvl.dat250.feedapp.dto.iot;

/**
 * Data transfer object for IoT requests
 * Used to transfer IoT requests between the iot and backend
 */
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
