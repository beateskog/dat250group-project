package no.hvl.dat250.feedapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.feedapp.dto.VoteDTO;
import no.hvl.dat250.feedapp.dto.iot.IoTRequest;
import no.hvl.dat250.feedapp.dto.iot.IoTResponse;
import no.hvl.dat250.feedapp.exception.AccessDeniedException;
import no.hvl.dat250.feedapp.exception.ResourceNotFoundException;
import no.hvl.dat250.feedapp.model.Poll;
import no.hvl.dat250.feedapp.model.Role;
import no.hvl.dat250.feedapp.model.Vote;
import no.hvl.dat250.feedapp.repository.PollRepository;
import no.hvl.dat250.feedapp.service.iot.IotAuthSercive;
import no.hvl.dat250.feedapp.service.PollService;
import no.hvl.dat250.feedapp.service.VoteService;

@RestController
@CrossOrigin(origins = "http://localhost:8000")
@RequestMapping("/iot")
public class IoTController {

    @Autowired
    private VoteService voteService;

    @Autowired
    private PollService pollService;

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private IotAuthSercive iotAuthSercive;

    @PostMapping("/votes")
    public ResponseEntity<?> createIoTVote(@RequestHeader("X-API-KEY") String apiKey,@RequestBody IoTResponse response) {
        try {
            if (!iotAuthSercive.isValidApiKey(apiKey)) {
                throw new AccessDeniedException("Invalid API key");
            }
            List<Vote> votes = voteService.createIoTVote(response);
            List<VoteDTO> voteDTOs = votes.stream().map(vote -> voteToVoteDTO(vote)).toList();
            return ResponseEntity.ok(voteDTOs);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @GetMapping("/random-question")
    public ResponseEntity<?> getRandomQuestion(@RequestHeader("X-API-KEY") String apiKey) {
        try {
            if (!iotAuthSercive.isValidApiKey(apiKey)) {
                throw new AccessDeniedException("Invalid API key");
            }
            List<Poll> publicPolls = pollService.findAllPublicPolls();
            
            int randomNumber = (int) (Math.random() * publicPolls.size());
            Poll randomPoll = publicPolls.get(randomNumber);
            String question = randomPoll.getQuestion();
            IoTRequest iotRequest = new IoTRequest();
            iotRequest.setQuestion(question);
            iotRequest.setPollId(randomPoll.getId());
            
            return ResponseEntity.ok(iotRequest);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @GetMapping("/{pollPin}")
    public ResponseEntity<?> getQuestion(@RequestHeader("X-API-KEY") String apiKey, @PathVariable int pollPin) {
        try {
            if (!iotAuthSercive.isValidApiKey(apiKey)) {
                throw new AccessDeniedException("Invalid API key");
            }
            Poll publicPoll = pollRepository.findPublicPollsByPollPin(pollPin)
                    .orElseThrow(() -> new ResourceNotFoundException("No public poll with the given pin: " + pollPin));
            
            IoTRequest iotRequest = new IoTRequest();
            iotRequest.setQuestion(publicPoll.getQuestion());
            iotRequest.setPollId(publicPoll.getId());
            
            return ResponseEntity.ok(iotRequest);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        } 
    }

    public VoteDTO voteToVoteDTO(Vote vote) {

        VoteDTO voteDTO = new VoteDTO();
        voteDTO.setId(vote.getId());
        if (vote.getAccount() == null) {
            voteDTO.setVoterId(null);
            voteDTO.setVoterRole(Role.ANONYMOUS_VOTER.toString());
        } else {
            voteDTO.setVoterId(vote.getAccount().getId());
            voteDTO.setVoterRole(vote.getAccount().getRole().toString());
        }
        voteDTO.setVote(vote.isVote());
        voteDTO.setVotingPlatform(vote.getPlatform());
        voteDTO.setPollId(vote.getPoll().getId());
        
        return voteDTO;
    }
    
}
