package no.hvl.dat250.feedapp;

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import no.hvl.dat250.feedapp.repositories.AccountRepository;
import no.hvl.dat250.feedapp.repositories.PollRepository;
import no.hvl.dat250.feedapp.repositories.VoteRepository;

@SpringBootApplication
public class FeedappApplication {

	private static final Logger log = LoggerFactory.getLogger(FeedappApplication.class);

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
            accountRepository.save(account);
            

            // Create and save Polls
            Poll poll = new Poll();
            poll.setQuestion("Do you like dogs better than cats?");
            poll.setPollPin(123);
            poll.setPollUrl("https://feedapp.no/poll/123");
            poll.setStartTime(LocalDateTime.now());
            poll.setEndTime(LocalDateTime.of(2023, 10, 10, 10, 10));
			poll.setPollOwner(account);
            account.getPolls().add(poll);
            pollRepository.save(poll);
            

            Poll poll2 = new Poll();
            poll2.setQuestion("Do you like cats better than dogs?");
            poll2.setPollPin(1234);
            poll2.setPollUrl("https://feedapp.no/poll/1234");
            poll2.setStartTime(LocalDateTime.now());
            poll2.setEndTime(LocalDateTime.of(2023, 10, 9, 10, 10));
			poll2.setPollOwner(account);
            pollRepository.save(poll2);
            

            // Create and save Votes
            Vote vote1 = new Vote();
            vote1.setVote(true);
            vote1.setVoteTime(LocalDateTime.now());
            vote1.setVotingPlatform(VotingPlatform.WEB);
            vote1.setPoll(poll);
            voteRepository.save(vote1);

            Vote vote2 = new Vote();
            vote2.setVote(true);
            vote2.setVoteTime(LocalDateTime.now());
            vote2.setVotingPlatform(VotingPlatform.IoT);
            vote2.setPoll(poll);
            voteRepository.save(vote2);
            

            Vote vote3 = new Vote();
            vote3.setVote(false);
            vote3.setVoteTime(LocalDateTime.now());
            vote3.setVotingPlatform(VotingPlatform.WEB);
            vote3.setPoll(poll2);
            voteRepository.save(vote3);


            // Associate entities
            account.getPolls().add(poll);
            account.getPolls().add(poll2);
			
            poll.getVotes().add(vote1);
            poll.getVotes().add(vote2);
            poll2.getVotes().add(vote3);

            
            // Fetch all accounts
            Iterable<Account> accounts = accountRepository.findAll();
            log.info("Accounts:");
            for (Account a : accounts) {
                log.info(a.toString());
            }

            // Fetch all polls
            Iterable<Poll> polls = pollRepository.findAll();
            log.info("Polls:");
            for (Poll p : polls) {
                log.info(p.toString());
            }

            // Fetch all votes
            Iterable<Vote> votes = voteRepository.findAll();
            log.info("Votes:");
            for (Vote v : votes) {
                log.info(v.toString());
            }

        };
    }
}
