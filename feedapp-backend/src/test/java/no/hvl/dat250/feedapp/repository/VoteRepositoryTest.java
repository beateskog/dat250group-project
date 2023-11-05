package no.hvl.dat250.feedapp.repository;
// There is no need to write unit test cases for the methods we are directly
// using from the JPA repository (such as FingById), because the JPA repository
// already has these methods implemented and tested.

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.model.Poll;
import no.hvl.dat250.feedapp.model.PollPrivacy;
import no.hvl.dat250.feedapp.model.Role;
import no.hvl.dat250.feedapp.model.Vote;
import no.hvl.dat250.feedapp.model.VotingPlatform;

@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class VoteRepositoryTest {

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
    public void setUp() {
        account = new Account(1L, "Username1", "********", Role.ADMIN);
        accountRepository.save(account);

        poll = new Poll(
            1L,
            1,
            "poll.com",
            "Do you like Christmas?",
            LocalDateTime.of(2023, 10, 10, 19, 0),
            LocalDateTime.of(2023, 12, 25, 20, 0),
            PollPrivacy.PUBLIC);
        poll.setAccount(account);
        pollRepository.save(poll);

        
        vote = new Vote(
            1L,
            true,
            VotingPlatform.WEB,
            LocalDateTime.of(2023, 10, 10, 19, 45));
        vote.setAccount(account);
        vote.setPoll(poll);
        voteRepository.save(vote);
    
    }

    @AfterEach
    public void tearDown() {
        accountRepository.deleteAll();
        
    }

    @Test
    public void testFindVoteByPollIdAndAccountId() {
        Vote foundvote = voteRepository.findVoteByPollIdAndAccountId(poll.getId(), account.getId());

        assertEquals(foundvote.getId(), vote.getId());
        assertEquals(foundvote.getAccount().getId(), vote.getAccount().getId());
        assertEquals(foundvote.getAccount().getUsername(), vote.getAccount().getUsername());
        assertEquals(foundvote.getPoll().getId(), vote.getPoll().getId());
        assertEquals(foundvote.getPoll().getQuestion(), vote.getPoll().getQuestion());
        assertEquals(foundvote.isVote(), vote.isVote());
    
    }

}
