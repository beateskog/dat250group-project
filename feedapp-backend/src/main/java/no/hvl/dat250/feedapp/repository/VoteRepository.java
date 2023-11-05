package no.hvl.dat250.feedapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import no.hvl.dat250.feedapp.model.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    //@Query("SELECT v FROM Vote v WHERE v.poll.id = :pollId AND v.account.id = :accountId")
    Vote findVoteByPollIdAndAccountId(Long pollId, Long accountId);
    
}