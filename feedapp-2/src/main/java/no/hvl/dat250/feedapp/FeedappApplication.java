package no.hvl.dat250.feedapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import no.hvl.dat250.feedapp.dto.authentication.RegisterRequestDTO;
import no.hvl.dat250.feedapp.service.jpa.AccountService;
import no.hvl.dat250.feedapp.service.jpa.AuthService;
import no.hvl.dat250.feedapp.service.jpa.PollService;
import no.hvl.dat250.feedapp.service.jpa.VoteService;


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
            
        };
    }

}
