package no.hvl.dat250.feedapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.feedapp.Poll;
import no.hvl.dat250.feedapp.Vote;
import no.hvl.dat250.feedapp.DTO.PollDTO;
import no.hvl.dat250.feedapp.repositories.PollRepository;

@RestController
@RequestMapping("/poll")
public class PollController {

    @Autowired
    private PollRepository pollRepository;

    @GetMapping("")
    public ResponseEntity<?>  getPollByPollPin(@RequestParam(value = "pollPin", required = true) String pollPin) {
        try {
            Poll poll = pollRepository.findByPollPin(Integer.parseInt(pollPin))
                .orElseThrow(() -> new RuntimeException("Poll with pin " + pollPin + " not found"));
            return ResponseEntity.ok(pollToPollDTO(poll));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getPollById(@PathVariable(value = "id", required = true) String id) {
        try {
            Poll poll = pollRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new RuntimeException("Poll with id " + id + " not found"));
            return ResponseEntity.ok(pollToPollDTO(poll));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePollById(@PathVariable(value = "id", required = true) String id) {
        try {
            Poll poll = pollRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new RuntimeException("Poll with id " + id + " not found"));
            poll.getPollOwner().getPolls().remove(poll);
            pollRepository.delete(poll);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        }
    }

    private PollDTO pollToPollDTO(Poll poll) {

        PollDTO pollDTO = new PollDTO();
        pollDTO.id = poll.getId();
        pollDTO.pollUrl = poll.getPollUrl();
        pollDTO.pollPin = poll.getPollPin();
        pollDTO.question = poll.getQuestion();
        pollDTO.startTime = poll.getStartTime().toString();
        pollDTO.endTime = poll.getEndTime().toString();
        pollDTO.privacy = poll.getPollPrivacy().toString();
        pollDTO.pollOwner = poll.getPollOwner().getUsername();
        pollDTO.totalVotes = poll.getVotes().size();
        pollDTO.yesVotes = 0;
        pollDTO.noVotes = 0;
        for (Vote vote : poll.getVotes()) {
            if (vote.isVote() == false) {
                pollDTO.noVotes++;
            } else {
                pollDTO.yesVotes++;
            }
        }

        return pollDTO;
    }

}
