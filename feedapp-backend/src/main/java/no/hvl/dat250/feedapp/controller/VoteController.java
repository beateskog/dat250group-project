package no.hvl.dat250.feedapp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.feedapp.dto.VoteDTO;
import no.hvl.dat250.feedapp.exception.BadRequestException;
import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.model.Role;
import no.hvl.dat250.feedapp.model.Vote;
import no.hvl.dat250.feedapp.service.VoteService;

@RestController
@RequestMapping("/vote")
public class VoteController {

    // The Controller layer communicates with service layer
    @Autowired
    VoteService voteService;

    // CREATE
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
            return ResponseEntity.status(HttpStatus.CREATED).body(voteToVoteDTO(createdVote));
        } catch (BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // READ
    @GetMapping("/{voteId}")
    public ResponseEntity<?> getVote(@PathVariable("voteId") Long voteId) {
        try {
            Vote vote = voteService.getVote(voteId);
            return ResponseEntity.ok(voteToVoteDTO(vote));
        } catch (BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllVotes() {
        try {
            List<Vote> votes = voteService.getAllVotes();
            List<VoteDTO> voteDTOs = votes.stream().map(vote -> voteToVoteDTO(vote)).toList();
            return ResponseEntity.ok(voteDTOs);
        } catch (BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    //Data Transfer object 
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
