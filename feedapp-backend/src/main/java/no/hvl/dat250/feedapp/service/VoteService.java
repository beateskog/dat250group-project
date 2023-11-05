package no.hvl.dat250.feedapp.service;

import java.util.List;

import no.hvl.dat250.feedapp.dto.VoteDTO;
import no.hvl.dat250.feedapp.dto.iot.IoTResponse;
import no.hvl.dat250.feedapp.model.Vote;


public interface VoteService {

    /**
     * Creates a vote and saves it to the database.
     * @param vote The voteDTO for the vote to be created.
     * @return The created vote.
     * @throws IllegalArgumentException if the vote is null. 
     * the pollId is null. the votingPlatform is null. 
     * Or if there already exists a vote with the given pollId and voterId.
     */
    public Vote createVote(VoteDTO vote);

     /**
      * Creates a vote and saves it to the database.
      * @param response The IoTResponse for the vote/votes to be created.
      * @return A list of the created votes.
      */
    public List<Vote> createIoTVote(IoTResponse response);

    /**
     * Updates a vote and saves it to the database.
     * @param vote The voteDTO for the vote to be updated.
     * @return The updated vote.
     */
    public Vote updateVote(VoteDTO vote);

    /**
     * Deletes a vote from the database.
     * @param voteId The ID of the vote to be deleted.
     * @return A string confirming that the vote was successfully deleted.
     */
    public String deleteVote(Long voteId);

    /**
     * Gets a vote from the database.
     * @param voteId The ID of the vote to be retrieved.
     * @return The vote with the given ID.
     */
    public Vote getVote(Long voteId);
    
    /**
     * Gets all votes from the database.
     * @return A list of all votes.
     */
    public List<Vote> getAllVotes();
}
