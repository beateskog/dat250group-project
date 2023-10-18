package no.hvl.dat250.feedapp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.feedapp.DTO.VoteDTO;
import no.hvl.dat250.feedapp.exception.BadRequestException;
import no.hvl.dat250.feedapp.model.Vote;
import no.hvl.dat250.feedapp.service.VoteService;
import no.hvl.dat250.feedapp.model.Role;

@RestController
@RequestMapping("/vote")
public class VoteController {

    // The Controller layer communicates with service layer
    VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<?> createVote(@RequestBody Vote vote) {
        try {
            Vote createdVote = voteService.createVote(vote);
            return ResponseEntity.status(HttpStatus.CREATED).body(voteToVoteDTO(createdVote));
        } catch (BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
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

    
    // UPDATE - Cannot update a vote
    // @PutMapping
    // public String updateVote(@RequestBody Vote vote) {
    //     voteService.updateVote(vote);
    //     return "Vote Updated Successfully";
    // }

    // DELETE - Cannot delete a vote
    // @DeleteMapping("{voteId}")
    // public String deleteVote(@PathVariable("voteId") Long voteId) {
    //     voteService.deleteVote(voteId);
    //     return "Vote Deleted Successfully";
    // }

    //Data Transfer object 
    public VoteDTO voteToVoteDTO(Vote vote) {

        VoteDTO voteDTO = new VoteDTO();
        voteDTO.id = vote.getId();
        if (vote.getAccount() == null) {
            voteDTO.voterId = null;
            voteDTO.voter = Role.ANONYMOUS_VOTER.toString();
        } else {
            voteDTO.voterId = vote.getAccount().getId();
            voteDTO.voter = vote.getAccount().getRole().toString();
        }
        voteDTO.vote = vote.isVote();
        voteDTO.pollId = vote.getPoll().getId();
        voteDTO.platform = vote.getPlatform();
        
        return voteDTO;
    }
}
