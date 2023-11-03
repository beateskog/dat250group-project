package no.hvl.dat250.feedapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import no.hvl.dat250.feedapp.model.Poll;


public interface PollRepository extends JpaRepository<Poll, Long> {

    // Skal vi ha med public eller fjerne det?
    public Optional<Poll> findPollByPollURL(String url);
    public Optional<Poll> findPollByPollPin(int pin);

    @Query("SELECT p FROM Poll p WHERE p.account.username = :username")
    public List<Poll> findPollsByOwnerUsername(@Param("username") String username);


    // ALL POLLS
    @Query("SELECT p FROM Poll p WHERE p.endTime >= current_date")
    public List<Poll> findAllPollsNotPassedEndTime();

    @Query("SELECT p FROM Poll p WHERE p.endTime < current_date")
    public List<Poll> findAllPollsPassedEndTime();


    // ONLY PUBLIC POLLS
    @Query("SELECT p FROM Poll p WHERE p.endTime >= current_date AND p.pollPrivacy = PUBLIC")
    public List<Poll> findPublicPollsNotPassedEndTime();

    @Query("SELECT p FROM Poll p WHERE p.endTime < current_date AND p.pollPrivacy = PUBLIC")
    public List<Poll> findPublicPollsPassedEndTime();

    @Query("SELECT p FROM Poll p WHERE p.pollPrivacy = PUBLIC")
    public List<Poll> findAllPublicPolls();

    @Query("SELECT p FROM Poll p WHERE p.endTime = current_date" )
    public List<Poll> findPollsEndToday();

    @Query("SELECT p FROM Poll p WHERE p.startTime >= current_date AND p.startTime < current_date + 1")
    public List<Poll> findPollsOpenToday();



  
}