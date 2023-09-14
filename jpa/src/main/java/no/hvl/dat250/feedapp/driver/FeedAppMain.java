package no.hvl.dat250.feedapp.driver;


import java.time.LocalDateTime;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import no.hvl.dat250.feedapp.Account;
import no.hvl.dat250.feedapp.Poll;
import no.hvl.dat250.feedapp.Role;
import no.hvl.dat250.feedapp.Vote;
import no.hvl.dat250.feedapp.VotingPlatform;

public class FeedAppMain {

  static final String PERSISTENCE_UNIT_NAME = "feedapp";

  public static void main(String[] args) {
    try (EntityManagerFactory factory = Persistence.createEntityManagerFactory(
        PERSISTENCE_UNIT_NAME); EntityManager em = factory.createEntityManager()) {
      em.getTransaction().begin();
      createObjects(em);
      em.getTransaction().commit();
    }
  }

  private static void createObjects(EntityManager em) {
        Account account = new Account();
        account.setUsername("test");
        account.setPassword("test");
        account.setEmail("myemail@outlook.com");
        account.setRole(Role.USER);
        em.persist(account);

        Poll poll = new Poll();
        poll.setQuestion("test");
        poll.setPollPin(123);
        poll.setPollUrl("https://test.com");
        poll.setStartTime(LocalDateTime.now());
        poll.setEndTime(LocalDateTime.of(2023, 10, 10, 10, 10));
        em.persist(poll);

        Poll poll2 = new Poll();
        poll2.setQuestion("test2");
        poll2.setPollPin(1234);
        poll2.setPollUrl("https://test2.com");
        poll2.setStartTime(LocalDateTime.now());
        poll2.setEndTime(LocalDateTime.of(2023, 10, 9, 10, 10));
        em.persist(poll2);

        Vote vote1 = new Vote();
        vote1.setVote(true);
        vote1.setVoteTime(LocalDateTime.now());
        vote1.setVotingPlatform(VotingPlatform.WEB);
        em.persist(vote1);

        Vote vote2 = new Vote();
        vote2.setVote(true);
        vote2.setVoteTime(LocalDateTime.now());
        vote2.setVotingPlatform(VotingPlatform.IoT);
        em.persist(vote2);

        Vote vote3 = new Vote();
        vote3.setVote(false);
        vote3.setVoteTime(LocalDateTime.now());
        vote3.setVotingPlatform(VotingPlatform.WEB);
        em.persist(vote3);

        account.getPolls().add(poll);
        account.getPolls().add(poll2);

        poll.getVotes().add(vote1);
        poll.getVotes().add(vote2);
        poll2.getVotes().add(vote3);

        poll.setPollOwner(account);
        poll2.setPollOwner(account);

        vote1.setOwningPoll(poll);
        vote2.setOwningPoll(poll);
        vote3.setOwningPoll(poll2);
    }
  }


