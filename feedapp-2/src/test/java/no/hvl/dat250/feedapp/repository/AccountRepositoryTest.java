package no.hvl.dat250.feedapp.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.model.Poll;
import no.hvl.dat250.feedapp.model.Role;
import no.hvl.dat250.feedapp.model.Vote;
import no.hvl.dat250.feedapp.model.VotingPlatform;

// There is no need to write unit test cases for the methods we are directly
// using from the JPA repository (such as FingById), because the JPA repository
// already has these methods implemented and tested (properly).

@DataJpaTest
public class AccountRepositoryTest {
    
 // Given - when - then

    @Autowired
    private AccountRepository accountRepository;
    Account account;

    @Autowired
    private PollRepository pollRepository;
    Poll poll;

    @Autowired
    private VoteRepository voteRepository;
    Vote vote;

    @BeforeEach
    void setUp() {
        account = new Account(1L, "Username1", "********", Role.ADMIN);
        accountRepository.save(account);

        poll = new Poll(
            1L,
            1234,
            "poll.com",
            "Do you like Christmas?",
            LocalDateTime.of(2023, 10, 10, 19, 0),
            LocalDateTime.of(2023, 12, 25, 20, 0));
        pollRepository.save(poll);
        
        vote = new Vote(
            1L,
            true,
            VotingPlatform.WEB,
            LocalDateTime.of(2023, 10, 10, 19, 45));
        voteRepository.save(vote);
    }

    @AfterEach
    void tearDown() {
        account = null;
        accountRepository.deleteAll();

        poll = null;
        pollRepository.deleteAll();

        vote = null;
        voteRepository.deleteAll();
    }

    // findByUsername(String) - SUCCESS
    @Test
    void testFindByUsername_Found() {
        Optional<Account> a = accountRepository.findAccountByUsername("Username1");
        assertThat(a.isPresent()).isTrue();
        assertThat(a.get().getUsername()).isEqualTo("Username1");
    }

    // findByUsername(String) - FAILURE
    @Test
    void testFindByUsername_NotFound() {
        Optional<Account> a = accountRepository.findAccountByUsername("Username2");
        assertThat(a.isPresent()).isFalse();
        // assertThat(a.isEmpty()).isTrue();
    }
}