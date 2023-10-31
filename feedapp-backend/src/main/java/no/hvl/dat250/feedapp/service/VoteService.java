package no.hvl.dat250.feedapp.service;

import java.util.List;

import no.hvl.dat250.feedapp.dto.VoteDTO;
import no.hvl.dat250.feedapp.dto.iot.IoTResponse;
import no.hvl.dat250.feedapp.model.Vote;


public interface VoteService {
    public Vote createVote(VoteDTO vote);

    public List<Vote> createIoTVote(IoTResponse response);
    public Vote updateVote(VoteDTO vote);
    public String deleteVote(Long voteId);
    public Vote getVote(Long voteId);
    public List<Vote> getAllVotes();
}
