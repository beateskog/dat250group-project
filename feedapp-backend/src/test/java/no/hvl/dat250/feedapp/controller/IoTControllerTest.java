package no.hvl.dat250.feedapp.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.hvl.dat250.feedapp.dto.iot.IoTResponse;
import no.hvl.dat250.feedapp.model.Poll;
import no.hvl.dat250.feedapp.model.Vote;
import no.hvl.dat250.feedapp.repository.PollRepository;
import no.hvl.dat250.feedapp.service.iot.IotSercive;
import no.hvl.dat250.feedapp.service.VoteService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class IoTControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VoteService voteService;

    @MockBean
    private PollRepository pollRepository;

    @MockBean
    private IotSercive iotSercive;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateIoTVote() throws Exception {
        // Given
        String apiKey = "validApiKey";
        IoTResponse response = new IoTResponse(); 
        Vote vote = new Vote(); 
        List<Vote> votes = List.of(vote);

        when(iotSercive.isValidApiKey(apiKey)).thenReturn(true);
        when(voteService.createIoTVote(response)).thenReturn(votes);

        // When & Then
        mockMvc.perform(post("/iot/votes")
                .header("X-API-KEY", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(response)))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateIoTVote_WrongAPIKey() throws Exception {
        IoTResponse response = new IoTResponse(); 

        mockMvc.perform(post("/iot/votes")
                .header("X-API-KEY", "")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(response)))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Invalid API key"));

    }

    @Test 
    public void testCreateIoTVote_missingAPIKey() throws Exception {
        IoTResponse response = new IoTResponse(); 

        mockMvc.perform(post("/iot/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(response)))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testGetQuestion_success() throws Exception {
        String apiKey = "validApiKey";
        int pollPin = 1234;
        Poll poll = new Poll(); 
        poll.setQuestion("Sample Question");
        poll.setId(1L);

        when(iotSercive.isValidApiKey(apiKey)).thenReturn(true);
        when(pollRepository.findPublicPollsByPollPin(pollPin)).thenReturn(Optional.of(poll));

    
        mockMvc.perform(get("/iot/" + pollPin)
                .header("X-API-KEY", apiKey))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"pollId\": 1, \"question\": \"Sample Question\"}"));
    }

    @Test
    public void testGetQuestion_wrongAPIKey() throws Exception {
        int pollPin = 1234;

        mockMvc.perform(get("/iot/" + pollPin)
                .header("X-API-KEY", ""))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Invalid API key"));
    }

    @Test
    public void testGetQuestion_noPollFound() throws Exception {
        int pollPin = 1234;
         String apiKey = "validApiKey";


        when(iotSercive.isValidApiKey(apiKey)).thenReturn(true);
        when(pollRepository.findPublicPollsByPollPin(pollPin)).thenReturn(Optional.empty());

    
        mockMvc.perform(get("/iot/" + pollPin)
                .header("X-API-KEY", apiKey))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetQuestion_missingAPIKey() throws Exception {
        int pollPin = 1234;

        mockMvc.perform(get("/iot/" + pollPin))
                .andExpect(status().isBadRequest());
    }



    

}
