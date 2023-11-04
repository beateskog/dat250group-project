package no.hvl.dat250.feedapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import no.hvl.dat250.feedapp.model.Poll;


public interface PollRepository extends JpaRepository<Poll, Long> {

    /**
     * Find a poll by its url
     * @param url the url of the poll
     * @return an optional that contains the poll if it exists
     */
    public Optional<Poll> findPollByPollURL(String url);

    /**
     * Find a poll by its pin
     * @param pin the pin of the poll
     * @return an optional that contains the poll if it exists
     */
    public Optional<Poll> findPollByPollPin(int pin);

    /**
     * Find a public poll by its id
     * @param id the id of the poll
     * @return an optional that contains the poll if it exists
     */
    @Query("SELECT p FROM Poll p WHERE p.pollPin = :pin AND p.pollPrivacy = PUBLIC")
    public Optional<Poll> findPublicPollsByPollPin(int pin);

    /**
     * Find all polls created by a given user
     * @param username
     * @return a list of all polls created by the given user
     */
    @Query("SELECT p FROM Poll p WHERE p.account.username = :username")
    public List<Poll> findPollsByOwnerUsername(@Param("username") String username);


    /**
     * Find all polls that have not passed their end time
     * @return a list of all polls that have not passed their end time
     */
    @Query("SELECT p FROM Poll p WHERE CAST(p.endTime AS DATE) > CURRENT_DATE")
    public List<Poll> findAllPollsNotPassedEndTime();

    /**
     * Find all polls that have passed their end time
     * @return a list of all polls that have passed their end time
     */
    @Query("SELECT p FROM Poll p WHERE CAST(p.endTime AS DATE) <= CURRENT_DATE")
    public List<Poll> findAllPollsPassedEndTime();

    /**
     * Find all public polls that have not passed their end time
     * @return a list of all public polls that have not passed their end time
     */
    @Query("SELECT p FROM Poll p WHERE CAST(p.endTime AS DATE) > CURRENT_DATE AND p.pollPrivacy = PUBLIC")
    public List<Poll> findPublicPollsNotPassedEndTime();

    /**
     * Find all public polls that have passed their end time
     * @return a list of all public polls that have passed their end time
     */
    @Query("SELECT p FROM Poll p WHERE CAST(p.endTime AS DATE) <= CURRENT_DATE AND p.pollPrivacy = PUBLIC")
    public List<Poll> findPublicPollsPassedEndTime();

    /**
     * Find all public polls
     * @return a list of all public polls
     */
    @Query("SELECT p FROM Poll p WHERE p.pollPrivacy = PUBLIC")
    public List<Poll> findAllPublicPolls();

    /**
     * Find all polls that has end time today
     * @return a list of all polls found
     */
    @Query("SELECT p FROM Poll p WHERE CAST(p.endTime AS DATE) = CURRENT_DATE")
    public List<Poll> findPollsEndToday();

    /**
     * Find all polls that has start time today
     * @return a list of all polls found
     */
    @Query("SELECT p FROM Poll p WHERE CAST(p.startTime AS DATE) = CURRENT_DATE")
    public List<Poll> findPollsOpenToday();



  
}