package no.hvl.dat250.feedapp;

import java.time.LocalDateTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.model.Poll;
import no.hvl.dat250.feedapp.model.PollPrivacy;
import no.hvl.dat250.feedapp.model.Role;
import no.hvl.dat250.feedapp.model.Vote;
import no.hvl.dat250.feedapp.model.VotingPlatform;
import no.hvl.dat250.feedapp.repository.AccountRepository;
import no.hvl.dat250.feedapp.repository.PollRepository;
import no.hvl.dat250.feedapp.repository.VoteRepository;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class FeedappApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeedappApplication.class, args);
	}

	@Bean
    @Transactional
    public CommandLineRunner demo(AccountRepository accountRepository, PollRepository pollRepository, VoteRepository voteRepository) {
        return (args) -> {
            // Create and save an Account
            Account account = new Account();
            account.setUsername("KariNordmann123");
            account.setPassword("IlikeDogs123");
            account.setRole(Role.USER);

            Account account2 = new Account();
            account2.setUsername("OlaNordmann123");
            account2.setPassword("IlikeCats123");
            account2.setRole(Role.USER);

            Account account3 = new Account();
            account3.setUsername("PerNordmann123");
            account3.setPassword("qwerty");
            account3.setRole(Role.USER);

            // Create and save Polls
            Poll poll = new Poll();
            poll.setQuestion("Do you like dogs better than cats?");
            poll.setPollPin(1);
            poll.setPollURL("https://feedapp.no/poll/1");
            poll.setStartTime(LocalDateTime.now());
            poll.setEndTime(LocalDateTime.of(2023, 10, 10, 10, 10));
            poll.setPollPrivacy(PollPrivacy.PUBLIC);
            poll.setAccount(account);
            
            Poll poll2 = new Poll();
            poll2.setQuestion("Do you like cats better than dogs?");
            poll2.setPollPin(2);
            poll2.setPollURL("https://feedapp.no/poll/2");
            poll2.setStartTime(LocalDateTime.now());
            poll2.setEndTime(LocalDateTime.of(2023, 10, 20, 10, 10));
            poll2.setPollPrivacy(PollPrivacy.PUBLIC);
            poll2.setAccount(account);

            Poll poll3 = new Poll();
            poll3.setQuestion("Do you sing in the shower?");
            poll3.setPollPin(3);
            poll3.setPollURL("https://feedapp.no/poll/3");
            poll3.setStartTime(LocalDateTime.now());
            poll3.setEndTime(LocalDateTime.of(2023, 8, 8, 10, 10));
            poll3.setPollPrivacy(PollPrivacy.PRIVATE);
            poll3.setAccount(account2);

            Vote vote1 = new Vote();
            vote1.setVote(true);
            vote1.setTimeOfVote(LocalDateTime.now());
            vote1.setPlatform(VotingPlatform.WEB);
            vote1.setPoll(poll);
            vote1.setAccount(account);
            
            Vote vote2 = new Vote();
            vote2.setVote(true);
            vote2.setTimeOfVote(LocalDateTime.now());
            vote2.setPlatform(VotingPlatform.IoT);
            vote2.setPoll(poll);
            vote2.setAccount(account2);
            
            Vote vote3 = new Vote();
            vote3.setVote(false);
            vote3.setTimeOfVote(LocalDateTime.now());
            vote3.setPlatform(VotingPlatform.WEB);
            vote3.setPoll(poll2);
            vote3.setAccount(account2);

            Vote vote4 = new Vote();
            vote4.setVote(false);
            vote4.setTimeOfVote(LocalDateTime.now());
            vote4.setPlatform(VotingPlatform.IoT);
            vote4.setPoll(poll2);
            vote4.setAccount(account);

            Vote vote5 = new Vote();
            vote5.setVote(true);
            vote5.setTimeOfVote(LocalDateTime.now());
            vote5.setPlatform(VotingPlatform.WEB);
            vote5.setPoll(poll2);
            vote5.setAccount(account3);

            Vote vote6 = new Vote();
            vote6.setVote(true);
            vote6.setTimeOfVote(LocalDateTime.now());
            vote6.setPlatform(VotingPlatform.IoT);
            vote6.setPoll(poll3);
            vote6.setAccount(account2);

            Vote vote7 = new Vote();
            vote7.setVote(true);
            vote7.setTimeOfVote(LocalDateTime.now());
            vote7.setPlatform(VotingPlatform.WEB);
            vote7.setPoll(poll3);
            vote7.setAccount(account3);
            
            poll.getVotes().add(vote1);
            poll.getVotes().add(vote2);
            poll2.getVotes().add(vote3);
            poll2.getVotes().add(vote4);
            poll2.getVotes().add(vote5);
            poll3.getVotes().add(vote6);
            poll3.getVotes().add(vote7);
            account2.getPolls().add(poll3);
            account.getPolls().add(poll);
            account.getPolls().add(poll2);
            account.getVotes().add(vote1);
            account.getVotes().add(vote4);
            account.getVotes().add(vote5);
            account2.getVotes().add(vote2);
            account2.getVotes().add(vote3);
            account2.getVotes().add(vote6);
            account3.getVotes().add(vote7);
            accountRepository.save(account);
            accountRepository.save(account2);
            accountRepository.save(account3);

        };
    }


}
