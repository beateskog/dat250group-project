package no.hvl.dat250.feedapp;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import no.hvl.dat250.feedapp.dto.PollDTO;
import no.hvl.dat250.feedapp.dto.VoteDTO;
import no.hvl.dat250.feedapp.dto.authentication.RegisterRequestDTO;
import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.model.PollPrivacy;
import no.hvl.dat250.feedapp.model.Role;
import no.hvl.dat250.feedapp.model.VotingPlatform;
import no.hvl.dat250.feedapp.repository.AccountRepository;
import no.hvl.dat250.feedapp.service.AccountService;
import no.hvl.dat250.feedapp.service.AuthService;
import no.hvl.dat250.feedapp.service.PollService;
import no.hvl.dat250.feedapp.service.VoteService;


@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableScheduling
public class FeedappApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeedappApplication.class, args);
	}

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

	@Bean
    @Transactional
    @Profile("!test")
    public CommandLineRunner demo(AccountService accountService, AuthService authService, PollService pollService, VoteService voteService, AccountRepository accountRepository) {
        return (args) -> {
            RegisterRequestDTO adminaccount = new RegisterRequestDTO("admin", "admin");
            authService.register(adminaccount);
            Account admin = accountService.findAccountByUsername("admin");
            admin.setRole(Role.ADMIN);
            accountRepository.save(admin);

            RegisterRequestDTO account = new RegisterRequestDTO("Kari", "Kari");
            authService.register(account);
            Account user = accountService.findAccountByUsername("Kari");

            PollDTO poll = new PollDTO();
            poll.setQuestion("Do you like dogs better than cats?");
            poll.setStartTime(LocalDateTime.of(2023, 11, 3, 19, 0));
            poll.setEndTime(LocalDateTime.of(2023, 12, 25, 20, 0));
            poll.setPollPrivacy(PollPrivacy.PUBLIC);
            pollService.createPoll(poll, user);

            RegisterRequestDTO account1 = new RegisterRequestDTO("test","test");
            authService.register(account1);
            Account user1 = accountService.findAccountByUsername("test");
            
            poll.setQuestion("Do you like Christmas?");
            poll.setStartTime(LocalDateTime.of(2023, 10, 10, 19, 0));
            poll.setEndTime(LocalDateTime.of(2023, 12, 25, 20, 0));
            poll.setPollPrivacy(PollPrivacy.PUBLIC);
            pollService.createPoll(poll, user1);

            poll.setQuestion("Do you like cats better than dogs?");
            poll.setPollPrivacy(PollPrivacy.PRIVATE);
            pollService.createPoll(poll, user1);
            
            RegisterRequestDTO account2 = new RegisterRequestDTO("Ola", "Ola");
            authService.register(account2);
            VoteDTO vote = new VoteDTO();
            vote.setVote(true);
            vote.setVotingPlatform(VotingPlatform.WEB);
            vote.setPollId(2L);
            vote.setVoterId(4L);
            voteService.createVote(vote);

            vote.setVote(false);
            vote.setVotingPlatform(VotingPlatform.WEB);
            vote.setPollId(1L);
            vote.setVoterId(4L);
            voteService.createVote(vote);

            vote.setVote(false);
            vote.setVotingPlatform(VotingPlatform.WEB);
            vote.setPollId(3L);
            vote.setVoterId(4L);
            voteService.createVote(vote);

            RegisterRequestDTO account3 = new RegisterRequestDTO("Per", "Per");
            authService.register(account3);
            vote.setVote(false);
            vote.setVotingPlatform(VotingPlatform.WEB);
            vote.setPollId(2L);
            vote.setVoterId(5L);
            voteService.createVote(vote);

            vote.setVote(false);
            vote.setVotingPlatform(VotingPlatform.WEB);
            vote.setPollId(1L);
            vote.setVoterId(5L);
            voteService.createVote(vote);

            vote.setVote(false);
            vote.setVotingPlatform(VotingPlatform.WEB);
            vote.setPollId(3L);
            vote.setVoterId(5L);
            voteService.createVote(vote);
            
        };
    }

}
