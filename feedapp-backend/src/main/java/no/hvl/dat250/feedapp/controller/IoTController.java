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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.feedapp.dto.VoteDTO;
import no.hvl.dat250.feedapp.dto.iot.IoTRequest;
import no.hvl.dat250.feedapp.dto.iot.IoTResponse;
import no.hvl.dat250.feedapp.exception.AccessDeniedException;
import no.hvl.dat250.feedapp.exception.BadRequestException;
import no.hvl.dat250.feedapp.exception.ResourceNotFoundException;
import no.hvl.dat250.feedapp.model.Poll;
import no.hvl.dat250.feedapp.model.Vote;
import no.hvl.dat250.feedapp.repository.PollRepository;
import no.hvl.dat250.feedapp.service.iot.IotSercive;
import no.hvl.dat250.feedapp.service.JwtService;
import no.hvl.dat250.feedapp.service.VoteService;

/**
 * The IoTController class is responsible for handling 
 * requests from and to the IoT device
 */
@RestController
@CrossOrigin(origins = "http://localhost:8000")
@RequestMapping("/iot")
public class IoTController {

    @Autowired
    private VoteService voteService;

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private IotSercive iotSercive;

    @Autowired
    private JwtService jwtservice;

    /**
     * Authenticates the IoT device
     * @param apiKey the api key to authenticate the request
     * @return a jwt token
     */
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestParam String apiKey) {
        try {
            if (iotSercive.isValidApiKey(apiKey)) {
                String jwt = jwtservice.generateTokenForApiKey(apiKey);
                return ResponseEntity.ok(jwt);
            } else {
                throw new AccessDeniedException("Invalid API key");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Authentication failed: " + e.getMessage());
        }
    }

    /**
     * Creates votes from an IoT device
     * @param apiKey the api key to authenticate the request
     * @param response the iot response object containing the votes and poll id
     * @return a list of the created votes as voteDTOs
     */
    @PostMapping("/votes")
    public ResponseEntity<?> createIoTVote(@RequestHeader("X-API-KEY") String apiKey,@RequestBody IoTResponse response) {
        try {
            if (!iotSercive.isValidApiKey(apiKey)) {
                throw new AccessDeniedException("Invalid API key");
            }
            List<Vote> votes = voteService.createIoTVote(response);
            List<VoteDTO> voteDTOs = votes.stream().map(vote -> VoteDTO.voteToVoteDTO(vote)).toList();
            return ResponseEntity.ok(voteDTOs);
        } catch (AccessDeniedException ex ) {
            return ResponseEntity.status(403).body(ex.getMessage());
        } catch(BadRequestException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }


    /**
     * Gets the question of a public poll
     * @param apiKey the api key to authenticate the request
     * @param pollPin the poll pin of the public poll
     * @return the iot request object containing the question and poll id
     */
    @GetMapping("/{pollPin}")
    public ResponseEntity<?> getQuestion(@RequestHeader("X-API-KEY") String apiKey, @PathVariable int pollPin) {
        try {
            if (!iotSercive.isValidApiKey(apiKey)) {
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
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(403).body(ex.getMessage());
        } catch (BadRequestException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        } 
    }
    
}
