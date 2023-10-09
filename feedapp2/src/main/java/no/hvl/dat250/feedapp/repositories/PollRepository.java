package no.hvl.dat250.feedapp.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import no.hvl.dat250.feedapp.Poll;
import no.hvl.dat250.feedapp.PollPrivacy;

public interface PollRepository extends JpaRepository<Poll, Long>{

    Optional<Poll> findByPollUrl(String pollUrl);

    Optional<Poll> findByPollPin(int pollPin);

    List<Poll> findByPollOwnerUsername(String username);

    @Query("SELECT p FROM Poll p WHERE p.endTime >= current_date")
    List<Poll> findPollsNotPassedEndTime();

    @Query("SELECT p FROM Poll p WHERE p.endTime < current_date")
    List<Poll> findPollsPassedEndTime();

    @Query("SELECT p FROM Poll p WHERE p.pollPrivacy = :privacy")
    List<Poll> findPublicPolls(@Param("privacy") PollPrivacy privacy);
}
