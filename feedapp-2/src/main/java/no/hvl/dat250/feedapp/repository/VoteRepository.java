package no.hvl.dat250.feedapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import no.hvl.dat250.feedapp.model.jpa.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    
}