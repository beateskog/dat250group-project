package no.hvl.dat250.feedapp.repository;

import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.model.Poll;
import no.hvl.dat250.feedapp.model.Role;
import no.hvl.dat250.feedapp.model.Vote;
import no.hvl.dat250.feedapp.model.VotingPlatform;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDateTime;



@DataJpaTest
@ActiveProfiles("test")
public class PollRepositoryTest {

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
            1234,
            "poll.com",
            "Do you like Christmas?",
            LocalDateTime.of(2023, 10, 10, 19, 0),
            LocalDateTime.of(2023, 12, 25, 20, 0));
        poll.setAccount(account);
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

   

 

    

}
