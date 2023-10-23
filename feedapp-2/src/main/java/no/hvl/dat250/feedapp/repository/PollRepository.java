package no.hvl.dat250.feedapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import no.hvl.dat250.feedapp.model.Poll;
import no.hvl.dat250.feedapp.model.PollPrivacy;


public interface PollRepository extends JpaRepository<Poll, Long> {

    // Skal vi ha med public eller fjerne det?
    public Optional<Poll> findPollByPollURL(String url);
    public Optional<Poll> findPollByPollPin(int pin);

    @Query("SELECT p FROM Poll p WHERE p.endTime >= current_date")
    public List<Poll> findPollsNotPassedEndTime();

    @Query("SELECT p FROM Poll p WHERE p.endTime < current_date")
    public List<Poll> findPollsPassedEndTime();

    @Query("SELECT p FROM Poll p WHERE p.pollPrivacy = :privacy")
    public List<Poll> findPublicPolls(@Param("privacy") PollPrivacy privacy);

    @Query("SELECT p FROM Poll p WHERE p.account.username = :username")
    public List<Poll> findPollsByOwnerUsername(@Param("username") String username);
    
    public List<Poll> findPollsByPollPrivacy(PollPrivacy public1);

}