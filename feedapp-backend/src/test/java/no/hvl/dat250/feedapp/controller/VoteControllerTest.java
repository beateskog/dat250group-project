package no.hvl.dat250.feedapp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.hvl.dat250.feedapp.dto.VoteDTO;
import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.model.Poll;
import no.hvl.dat250.feedapp.model.Role;
import no.hvl.dat250.feedapp.model.Vote;
import no.hvl.dat250.feedapp.model.VotingPlatform;
import no.hvl.dat250.feedapp.repository.AccountRepository;
import no.hvl.dat250.feedapp.service.VoteService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class VoteControllerTest {

    @MockBean
    private VoteService voteService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountRepository accountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    Account account;
    Poll poll;
    Vote vote;

    @BeforeEach
    public void setup() {
        vote = new Vote();
        vote.setId(1L);
        vote.setVote(true);
        vote.setPoll(poll);
        vote.setAccount(account);
        vote.setPlatform(VotingPlatform.WEB);

        account = new Account();
        account.setId(1L);
        account.setRole(Role.USER);
        account.setUsername("test");

        poll = new Poll();
        poll.setId(1L);
        poll.setQuestion("test");
        poll.setAccount(account);
        poll.setStartTime(LocalDateTime.now());
        poll.setEndTime(LocalDateTime.now().plusDays(1));
    }

    @AfterEach
    public void cleanup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testCreateVote_AuthenticatedUser() throws Exception {
        VoteDTO voteDTO = new VoteDTO();
        voteDTO.setVote(true);
        voteDTO.setPollId(1L);
        voteDTO.setVotingPlatform(VotingPlatform.WEB);

        vote.setId(1L);
        vote.setVote(true);
        vote.setPoll(poll);
        vote.setAccount(account);
        vote.setPlatform(VotingPlatform.WEB);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(account, null, List.of());
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        when(voteService.createVote(any(VoteDTO.class))).thenReturn(vote);

        mockMvc.perform(post("/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(voteDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(VoteDTO.voteToVoteDTO(vote))));

        verify(voteService).createVote(argThat(v -> v.getVoterId().equals(account.getId()) && v.getVoterRole().equals(account.getRole().toString())));
    }

    @Test
    public void testCreateVote_AnonymousUser() throws Exception {
        VoteDTO voteDTO = new VoteDTO();
        voteDTO.setVote(true);
        voteDTO.setPollId(1L);
        voteDTO.setVotingPlatform(VotingPlatform.WEB);

        Vote vote = new Vote();
        vote.setId(1L);
        vote.setVote(true);
        vote.setPoll(poll);
        vote.setPlatform(VotingPlatform.WEB);

        when(voteService.createVote(any(VoteDTO.class))).thenReturn(vote);

        mockMvc.perform(post("/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(voteDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(VoteDTO.voteToVoteDTO(vote))));

        verify(voteService).createVote(argThat(v -> v.getVoterRole().equals(Role.ANONYMOUS_VOTER.toString())));
    }

    @Test
    public void testCreateVote_InvalidPollId() throws Exception {
        VoteDTO voteDTO = new VoteDTO();
        voteDTO.setVote(true);
        voteDTO.setPollId(null);
        voteDTO.setVotingPlatform(VotingPlatform.WEB);

        when(voteService.createVote(any(VoteDTO.class))).thenThrow(new IllegalArgumentException("Invalid Poll ID"));

        mockMvc.perform(post("/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(voteDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid Poll ID"));
    }

    @Test
    public void testCreateVote_VotingPlatformIsNull() throws Exception {
        VoteDTO voteDTO = new VoteDTO();
        voteDTO.setVote(true);
        voteDTO.setPollId(1L);
        voteDTO.setVotingPlatform(null);

        when(voteService.createVote(any(VoteDTO.class))).thenThrow(new IllegalArgumentException("Error Message"));

        mockMvc.perform(post("/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(voteDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error Message"));
    }

    @Test 
    public void testGetVote() throws Exception {
        vote.setId(1L);
        vote.setVote(true);
        vote.setPoll(poll);
        vote.setAccount(account);
        vote.setPlatform(VotingPlatform.WEB);

        when(voteService.getVote(1L)).thenReturn(vote);

        mockMvc.perform(get("/vote/" + 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(VoteDTO.voteToVoteDTO(vote))));
    }


    
    
}

