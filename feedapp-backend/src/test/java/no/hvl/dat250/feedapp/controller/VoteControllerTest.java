// package no.hvl.dat250.feedapp.controller;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.when;
// import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import java.util.Collections;

// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.context.junit.jupiter.SpringExtension;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.http.MediaType;

// import com.fasterxml.jackson.databind.ObjectMapper;

// import no.hvl.dat250.feedapp.dto.VoteDTO;
// import no.hvl.dat250.feedapp.exception.BadRequestException;
// import no.hvl.dat250.feedapp.model.Account;
// import no.hvl.dat250.feedapp.model.Poll;
// import no.hvl.dat250.feedapp.model.Role;
// import no.hvl.dat250.feedapp.model.Vote;
// import no.hvl.dat250.feedapp.model.VotingPlatform;
// import no.hvl.dat250.feedapp.repository.AccountRepository;
// import no.hvl.dat250.feedapp.service.AccountService;
// import no.hvl.dat250.feedapp.service.VoteService;

// @ExtendWith(SpringExtension.class)
// @SpringBootTest
// @AutoConfigureMockMvc
// @ActiveProfiles("test")
// public class VoteControllerTest {

//     @MockBean
//     private VoteService voteService;

//     @MockBean
//     private UserDetailsService userDetailsService;

//     @Autowired
//     private MockMvc mockMvc;

//     @MockBean
//     private AccountRepository accountRepository;

//     @Autowired
//     private ObjectMapper objectMapper;

//     @AfterEach
//     public void cleanup() {
//         SecurityContextHolder.clearContext();
//     }

//    @Test
//     public void testCreateVote_Success_Authenticated() throws Exception {
//         Poll poll = new Poll();
//         poll.setId(1L);

//         VoteDTO inputVote = new VoteDTO();
//         inputVote.setVote(true);
//         inputVote.setPollId(1L);
//         inputVote.setVotingPlatform(VotingPlatform.WEB);

//         Vote outputVote = new Vote(); 
//         outputVote.setId(1L);
//         outputVote.setVote(true);
//         outputVote.setPoll(poll);

//         VoteDTO outputVoteDTO = new VoteDTO();
//         outputVoteDTO.setId(1L);
//         outputVoteDTO.setVote(true);
//         outputVoteDTO.setPollId(1L);
//         outputVoteDTO.setVoterId(1L);
//         outputVoteDTO.setVoterRole(Role.USER.toString());

//         Account mockAccount = new Account();
//         mockAccount.setId(1L);
//         mockAccount.setRole(Role.USER);
//         Authentication auth = new UsernamePasswordAuthenticationToken(mockAccount, null, Collections.emptyList());
        
//         when(voteService.createVote(any())).thenReturn(outputVote);

//         mockMvc.perform(post("/vote")
//                 .with(authentication(auth))
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(inputVote)))
//                 .andExpect(status().isCreated())
//                 .andExpect(content().json(objectMapper.writeValueAsString(inputVote))); // Adjust as per your `voteToVoteDTO` conversion
//     }

    
//     public void testCreateVote_Success_Anonymous() throws Exception {
//         VoteDTO inputVote = new VoteDTO(); // Initialize with sample data
//         Vote outputVote = new Vote(); // Initialize with sample data

//         when(voteService.createVote(any())).thenReturn(outputVote);

//         mockMvc.perform(post("/vote")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(inputVote)))
//                 .andExpect(status().isCreated())
//                 .andExpect(content().json(objectMapper.writeValueAsString(inputVote))); // Adjust as per your `voteToVoteDTO` conversion
//     }

  
//     public void testCreateVote_Fail_BadRequest() throws Exception {
//         VoteDTO inputVote = new VoteDTO(); // Initialize with sample data

//         when(voteService.createVote(any())).thenThrow(new BadRequestException("Invalid data"));

//         mockMvc.perform(post("/vote")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(inputVote)))
//                 .andExpect(status().isBadRequest())
//                 .andExpect(content().string("Invalid data"));
//     }
// }

