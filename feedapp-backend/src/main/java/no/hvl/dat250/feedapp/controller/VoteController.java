package no.hvl.dat250.feedapp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.feedapp.dto.VoteDTO;
import no.hvl.dat250.feedapp.exception.BadRequestException;
import no.hvl.dat250.feedapp.exception.ResourceNotFoundException;
import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.model.Role;
import no.hvl.dat250.feedapp.model.Vote;
import no.hvl.dat250.feedapp.service.VoteService;

/**
 * The VoteController class is responsible for handling requests from the client
 * related to the Vote model. The controller layer is responsible for
 * communicating with the service layer.
 */
@RestController
@RequestMapping("/vote")
@CrossOrigin(origins = "http://localhost:4200")
public class VoteController {

    @Autowired
    VoteService voteService;

    // CREATE
    /**
     * Creates a new vote. If the user is authenticated, the vote will be linked to the user, else the vote will be anonymous.
     * @param authentication authentication object 
     * @param vote The VoteDTO object that contains the information about the vote to be created.
     * @return Returns the created vote as a VoteDTO object.
     */
    @PostMapping
    public ResponseEntity<?> createVote(Authentication authentication, @RequestBody VoteDTO vote) {
        try {
            if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
                Account account = (Account) authentication.getPrincipal();
                vote.setVoterId(account.getId());
                vote.setVoterRole(account.getRole().toString());
            } else {
                vote.setVoterRole(Role.ANONYMOUS_VOTER.toString());
            }
            Vote createdVote = voteService.createVote(vote);
            return ResponseEntity.status(HttpStatus.CREATED).body(VoteDTO.voteToVoteDTO(createdVote));
        } catch (BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // READ
    /**
     * Finds a vote by its ID.
     * @param voteId The ID of the vote to be found.
     * @return Returns the vote with the given ID as a VoteDTO object.
     * @throws BadRequestException If a vote with the given ID does not exist.
     * @throws IllegalArgumentException If the voteId is null
     * @throws ResourceNotFoundException If a vote with the given ID does not exist.
     * @throws Exception If something unexpected happens.
     */
    @GetMapping("/{voteId}")
    public ResponseEntity<?> getVote(@PathVariable("voteId") Long voteId) {
        try {
            Vote vote = voteService.getVote(voteId);
            return ResponseEntity.ok(VoteDTO.voteToVoteDTO(vote));
        } catch (BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    /**
     * Finds all votes in the database.
     * @return Returns a list of all votes in the database as a list of VoteDTO objects.
     */
    @GetMapping
    public ResponseEntity<?> getAllVotes() {
        try {
            List<Vote> votes = voteService.getAllVotes();
            List<VoteDTO> voteDTOs = votes.stream().map(vote -> VoteDTO.voteToVoteDTO(vote)).toList();
            return ResponseEntity.ok(voteDTOs);
        } catch (BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

}
