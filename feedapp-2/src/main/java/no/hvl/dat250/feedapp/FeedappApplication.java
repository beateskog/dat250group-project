package no.hvl.dat250.feedapp;

import java.time.LocalDateTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import no.hvl.dat250.feedapp.DTO.PollDTO;
import no.hvl.dat250.feedapp.DTO.authentication.RegisterRequestDTO;
import no.hvl.dat250.feedapp.model.PollPrivacy;
import no.hvl.dat250.feedapp.model.Vote;
import no.hvl.dat250.feedapp.model.VotingPlatform;
import no.hvl.dat250.feedapp.service.AccountService;
import no.hvl.dat250.feedapp.service.AuthService;
import no.hvl.dat250.feedapp.service.PollService;
import no.hvl.dat250.feedapp.service.VoteService;


@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class FeedappApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeedappApplication.class, args);
	}

	@Bean
    @Transactional
    @Profile("!test")
    public CommandLineRunner demo(AccountService accountService, AuthService authService, PollService pollService, VoteService voteService) {
        return (args) -> {
            // Create and save an Account
            RegisterRequestDTO account = new RegisterRequestDTO("KariNordmann123", "IlikeDogs123");
            authService.register(account);

            RegisterRequestDTO account1 = new RegisterRequestDTO("test","test");
            authService.register(account1);

            RegisterRequestDTO account2 = new RegisterRequestDTO("OlaNordmann123", "IlikeCats123");
            authService.register(account2);

            RegisterRequestDTO account3 = new RegisterRequestDTO("PerNordmann123", "qwerty");
            authService.register(account3);


            // Create and save Polls
            PollDTO poll = new PollDTO();
            poll.setQuestion("Do you like dogs better than cats?");
            poll.setStartTime(LocalDateTime.now());
            poll.setEndTime(LocalDateTime.of(2023, 10, 10, 10, 10));
            poll.setPollPrivacy(PollPrivacy.PUBLIC);
            poll.setPollOwnerId(1L);
            pollService.createPoll(poll);
            
            PollDTO poll2 = new PollDTO();
            poll2.setQuestion("Do you like cats better than dogs?");
            poll2.setStartTime(LocalDateTime.now());
            poll2.setEndTime(LocalDateTime.of(2023, 10, 20, 10, 10));
            poll2.setPollPrivacy(PollPrivacy.PUBLIC);
            poll2.setPollOwnerId(1L);

            PollDTO poll3 = new PollDTO();
            poll3.setQuestion("Do you sing in the shower?");
            poll3.setStartTime(LocalDateTime.now());
            poll3.setEndTime(LocalDateTime.of(2023, 8, 8, 10, 10));
            poll3.setPollPrivacy(PollPrivacy.PRIVATE);
            poll3.setPollOwnerId(2L);

            Vote vote1 = new Vote();
            vote1.setVote(true);
            vote1.setPlatform(VotingPlatform.WEB);
            //voteService.createVote(vote1);
            
            Vote vote2 = new Vote();
            vote2.setVote(true);
            vote2.setPlatform(VotingPlatform.IoT);
           
            Vote vote3 = new Vote();
            vote3.setVote(false);
            vote3.setPlatform(VotingPlatform.WEB);

            Vote vote4 = new Vote();
            vote4.setVote(false);
            vote4.setPlatform(VotingPlatform.IoT);

            Vote vote5 = new Vote();
            vote5.setVote(true);
            vote5.setPlatform(VotingPlatform.WEB);

            Vote vote6 = new Vote();
            vote6.setVote(true);
            vote6.setPlatform(VotingPlatform.IoT);

            Vote vote7 = new Vote();
            vote7.setVote(true);
            vote7.setPlatform(VotingPlatform.WEB);
            
        };
    }


}
