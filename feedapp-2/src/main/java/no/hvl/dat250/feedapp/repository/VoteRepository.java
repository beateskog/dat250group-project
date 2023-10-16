package no.hvl.dat250.feedapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import no.hvl.dat250.feedapp.model.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    
}