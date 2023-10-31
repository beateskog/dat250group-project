package no.hvl.dat250.feedapp.repository;

import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.model.Poll;
import no.hvl.dat250.feedapp.model.PollPrivacy;
import no.hvl.dat250.feedapp.model.Role;
import no.hvl.dat250.feedapp.model.Vote;
import no.hvl.dat250.feedapp.model.VotingPlatform;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Optional;



@DataJpaTest
@ActiveProfiles("test")
// this annotation resets the database before each test method, slowing down the tests, but making them independent
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PollRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;
    Account account;

    @Autowired
    private PollRepository pollRepository;
    Poll poll;
    Poll poll2;
    Poll poll3;

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

        poll2 = new Poll(
            2L,
            2,
            "poll2.com",
            "Do you like ham?",
            LocalDateTime.of(2022, 10, 10, 19, 0),
            LocalDateTime.of(2022, 12, 25, 20, 0),
            PollPrivacy.PRIVATE);
        poll2.setAccount(account);
        pollRepository.save(poll2);

        poll3 = new Poll(
            3L,
            3,
            "poll3.com",
            "Do you like ham?",
            LocalDateTime.of(2021, 10, 10, 19, 0),
            LocalDateTime.of(2021, 12, 25, 20, 0),
            PollPrivacy.PUBLIC);
        poll3.setAccount(account);
        pollRepository.save(poll3);
        
        vote = new Vote(
            1L,
            true,
            VotingPlatform.WEB,
            LocalDateTime.of(2023, 10, 10, 19, 45));
        voteRepository.save(vote);
    
    }

    @AfterEach
    public void tearDown() {
        //Because of cascadeType.ALL on accounts, polls and votes will be deleted as well
        accountRepository.deleteAll();
        

    }

    @Test
    void testFindByUrl_Found() {
        Optional<Poll> p = pollRepository.findPollByPollURL("poll.com");
        assertThat(p).isNotNull();
        assertThat(p.get().getPollURL()).isEqualTo("poll.com");
    }

    @Test
    void testFindByUrl_NotFound() {
        Optional<Poll> p = pollRepository.findPollByPollURL("no.com");
        assertThat(p).isEmpty();
    }

    @Test
    void testFindByPin_Found() {
        Optional<Poll> p = pollRepository.findPollByPollPin(1);
        assertThat(p).isNotNull();
        assertThat(p.get().getPollPin()).isEqualTo(1);
    }

    @Test
    void testFindByPin_NotFound() {
        Optional<Poll> p = pollRepository.findPollByPollPin(1235);
        assertThat(p).isEmpty();
    }

    @Test
    void testFindPollsByOwnerUsername_Found() {
        Optional<Account> a = accountRepository.findAccountByUsername("Username1");
        assertThat(a.isPresent()).isTrue();
        assertThat(a.get().getUsername()).isEqualTo("Username1");

        Optional<Poll> p = pollRepository.findPollByPollURL("poll.com");
        assertThat(p).isNotNull();
        assertThat(p.get().getPollURL()).isEqualTo("poll.com");

        assertThat(pollRepository.findPollsByOwnerUsername("Username1")).isNotNull();
        assertThat(pollRepository.findPollsByOwnerUsername("Username1").get(0).getPollURL()).isEqualTo("poll.com");
    }

    @Test
    void testFindPollsByOwnerUsername_NotFound() {
        Optional<Account> a = accountRepository.findAccountByUsername("Username2");
        assertThat(a.isPresent()).isFalse();

        assertThat(pollRepository.findPollsByOwnerUsername("Username2")).isEmpty();
    }

    @Test
    void testFindAllPollsNotPassedEndTime() {
        assertThat(pollRepository.findAllPollsNotPassedEndTime()).isNotNull();
        assertThat(pollRepository.findAllPollsNotPassedEndTime().get(0).getPollURL()).isEqualTo("poll.com");
    }

    @Test
    void testFindAllPollsPassedEndTime() {
        assertThat(pollRepository.findAllPollsPassedEndTime()).isNotNull();
        assertThat(pollRepository.findAllPollsPassedEndTime().get(0).getPollURL()).isEqualTo("poll2.com");
    }

    @Test
    void testFindPublicPollsNotPassedEndTime() {
        assertThat(pollRepository.findPublicPollsNotPassedEndTime()).isNotNull();
        assertThat(pollRepository.findPublicPollsNotPassedEndTime().get(0).getPollURL()).isEqualTo("poll.com");
    }

    @Test
    void testFindPublicPollsPassedEndTime() {
        assertThat(pollRepository.findPublicPollsPassedEndTime()).isNotNull();
        assertThat(pollRepository.findPublicPollsPassedEndTime().get(0).getPollURL()).isEqualTo("poll3.com");
    }

    @Test
    void testFindAllPublicPolls() {
        assertThat(pollRepository.findAllPublicPolls()).isNotNull();
        assertThat(pollRepository.findAllPublicPolls().get(0).getPollURL()).isEqualTo("poll.com");
        assertThat(pollRepository.findAllPublicPolls().get(1).getPollURL()).isEqualTo("poll3.com");
    }



   

 

    

}
