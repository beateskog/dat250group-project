package no.hvl.dat250.feedapp.service;

import java.util.List;

import no.hvl.dat250.feedapp.dto.PollDTO;
import no.hvl.dat250.feedapp.exception.ResourceNotFoundException;
import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.model.Poll;


public interface PollService {

    // CREATE
    /**
     * Creates a poll and saves it to the database.
     * @param pollDTO The pollDTO for the poll to be created.
     * @param account The account of the user creating the poll.
     * @return The created poll.
     * @throws IllegalArgumentException If any of the required fields are null.
     */
    public Poll createPoll(PollDTO poll, Account account);

    // READ
    /**
     * Finds a poll by its ID.
     * @param pollId The ID of the poll to be found.
     * @return The found poll.
     * @throws ResourceNotFoundException If no poll with the given ID exists.
     */
    public Poll findPollById(Long pollId);

     /**
     * Finds a poll by its URL.
     * @param url The URL of the poll to be found.
     * @return The found poll.
     * @throws ResourceNotFoundException If no poll with the given URL exists.
     */
    public Poll findPollByUrl(String url);

    /**
     * Finds a poll by its pin.
     * @param pin The pin of the poll to be found.
     * @return The found poll.
     * @throws ResourceNotFoundException If no poll with the given pin exists.
     */
    public Poll findPollByPin(int pin);

    /**
     * Finds all the polls created by a given user.
     * @param username The username of the user to find polls for.
     * @return A list of all polls created by the given user.
     * If no polls are found, an empty list is returned.
     */
    public List<Poll> findPollsByOwnerUsername(String username);

    /**
     * Finds all polls that have not passed their end time.
     * @return A list of all polls that have not passed their end time.
     * If no polls are found, an empty list is returned.
     */
    public List<Poll> findAllPollsNotPassedEndTime();
    /**
     * Finds all polls that have passed their end time.
     * @return A list of all polls that have passed their end time.
     * If no polls are found, an empty list is returned.
     */
    public List<Poll> findAllPollsPassedEndTime();

    /**
     * finds all polls in the database.
     * @return A list of all polls.
     * If no polls are found, an empty list is returned.
     */
    public List<Poll> findAllPolls();

    /**
     * Finds all public polls that have not passed their end time.
     * @return A list of all public polls that have not passed their end time.
     * If no polls are found, an empty list is returned.
     */
    public List<Poll> findPublicPollsNotPassedEndTime();

    /**
     * Finds all public polls that have passed their end time.
     * @return A list of all public polls that have passed their end time.
     * If no polls are found, an empty list is returned.
     */
    public List<Poll> findPublicPollsPassedEndTime();
    
    /**
     * Finds all public polls.
     * @return A list of all public polls.
     * If no polls are found, an empty list is returned.
     */
    public List<Poll> findAllPublicPolls();

    /**
     * Finds the public poll with the given ID.
     * @return The public poll with the given ID.
     */
    public Poll findPublicPollById(Long pollId);

    /**
     * Goes through all polls in the database that ends today
     * and posts a pollClosedEvent to dweet.io if the poll has ended today.
     */
    public void dweetPollsEndToday();

     /**
     * Goes through all polls in the database that starts today
     * and posts a pollOpenedEvent to dweet.io if the poll has started today.
     */
    public void dweetPollsOpenToday();

    /**
     * Goes through all polls in the database that ends today
     * and sends a message to the pollResultsExchange if the poll has ended today.
     */
    public void savePollsEndToday();

    // UPDATE
    /**
     * Updates a poll in the database.
     * @param poll The pollDTO for the poll to be updated.
     * @return The updated poll.
     * @throws IllegalArgumentException If the poll ID is null.
     * @throws ResourceNotFoundException If no poll with the given ID exists.
     * @throws IllegalArgumentException If the start time is after the end time.
     * @throws IllegalArgumentException If the start time is before the current time.
     * @throws IllegalArgumentException If the end time is before the start time.
     * @throws IllegalArgumentException If the end time is before the current time.
     */
    public Poll updatePoll(PollDTO poll);

    // DELETE
    /**
     * Deletes a poll from the database.
     * @param pollId The ID of the poll to be deleted.
     * @param user The account of the user deleting the poll. Must be admin. 
     * @return A string confirming that the poll has been deleted.
     * @throws IllegalArgumentException If the user does not have permission to delete the poll.
     * @throws ResourceNotFoundException If no poll with the given ID exists.
     */
    public String deletePollById(Long pollId, Account user);
      
}
