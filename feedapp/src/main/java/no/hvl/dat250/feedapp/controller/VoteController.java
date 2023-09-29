package no.hvl.dat250.feedapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.feedapp.Vote;
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
            return ResponseEntity.ok(votes);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
        
}

    


    

