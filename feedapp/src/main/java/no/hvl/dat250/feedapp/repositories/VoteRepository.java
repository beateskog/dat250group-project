package no.hvl.dat250.feedapp.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import no.hvl.dat250.feedapp.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long>{

    Optional<List<Vote>> findByPollId(Long id);

}
