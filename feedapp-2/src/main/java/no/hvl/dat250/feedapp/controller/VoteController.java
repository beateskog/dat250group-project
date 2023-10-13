package no.hvl.dat250.feedapp.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.feedapp.model.Vote;
import no.hvl.dat250.feedapp.service.VoteService;

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
    public String createVote(@RequestBody Vote vote) {
        voteService.createVote(vote);
        return "Vote Created Successfully";
    }

    // READ
    @GetMapping("{voteId}")
    public Vote getVote(@PathVariable("voteId") Long voteId) {
        return voteService.getVote(voteId);
    }

    @GetMapping
    public List<Vote> getAllVotes() {
        return voteService.getAllVotes();
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
}
