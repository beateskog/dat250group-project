package no.hvl.dat250.feedapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import no.hvl.dat250.feedapp.Poll;

public interface PollRepository extends JpaRepository<Poll, Long>{

    Optional<Poll> findByPollUrl(String pollUrl);

    Optional<Poll> findByPollPin(int pollPin);

}
