package no.hvl.dat250.feedapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.feedapp.Vote;
import no.hvl.dat250.feedapp.DTO.VoteDTO;
import no.hvl.dat250.feedapp.repositories.VoteRepository;

@RestController
public class VoteController {

    @Autowired
    private VoteRepository voteRepository;

    @GetMapping("poll/{id}/votes")
    public ResponseEntity<?> getVotesByPollId(@PathVariable String id) {
        try {
            List<Vote> votes = voteRepository.findByPollId(Long.parseLong(id))
                    .orElseThrow(() -> new RuntimeException("Poll with id " + id + " not found"));
            List<VoteDTO> voteDTOs = votes.stream().map(vote -> voteToVoteDTO(vote)).toList();
            return ResponseEntity.ok(voteDTOs);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public VoteDTO voteToVoteDTO(Vote vote) {

        VoteDTO voteDTO = new VoteDTO();
        voteDTO.id = vote.getId();
        voteDTO.voter = vote.getAccount().getRole().toString();
        voteDTO.voterId = vote.getAccount().getId();
       
        if (vote.isVote() == false) {
            voteDTO.vote = "No";
        } else {
            voteDTO.vote = "Yes";
        }
        voteDTO.pollId = vote.getPoll().getId();
        

        return voteDTO;
    }
        
}

    


    

