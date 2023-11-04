package no.hvl.dat250.feedapp.dto.iot;

/**
 * Data transfer object for IoT responses
 * Used to transfer IoT responses between the iot and backend
 */
public class IoTResponse {

    private Long pollId;
    private int yesVotes;
    private int noVotes;
    
    public void setPollId(Long pollId) {
        this.pollId = pollId;
    }

    public void setYesVotes(int yesVotes) {
        this.yesVotes = yesVotes;
    }

    public void setNoVotes(int noVotes) {
        this.noVotes = noVotes;
    }

    public Long getPollId() {
        return pollId;
    }

    public int getYesVotes() {
        return yesVotes;
    }

    public int getNoVotes() {
        return noVotes;
    }

}
