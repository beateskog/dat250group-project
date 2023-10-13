package no.hvl.dat250.feedapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.feedapp.exception.BadRequestException;
import no.hvl.dat250.feedapp.exception.ResourceNotFoundException;
import no.hvl.dat250.feedapp.model.Poll;
import no.hvl.dat250.feedapp.model.PollPrivacy;
import no.hvl.dat250.feedapp.service.PollService;

@RestController
@RequestMapping("/poll") // All request mappings start with poll
public class PollController {

    // The Controller layer communicates with the service layer
    PollService pollService;

    public PollController(PollService pollService) {
        this.pollService = pollService;
    }
    
    
    // CREATE
    @PostMapping
    public ResponseEntity<?> createPoll(@RequestBody Poll poll) {
        try {
            pollService.createPoll(poll);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (BadRequestException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // READ
    @GetMapping("{pollId}")
    public Poll findPollById(@PathVariable("pollId") Long pollId) {
        return pollService.findPollById(pollId);

        // return ResponseHandler.responseBuilder(
        //     "Requested Poll Details are given here.",
        //     HttpStatus.OK,
        //     pollService.getPoll(pollId));
    }

    @GetMapping("/url")
    public ResponseEntity<Poll> findPollByUrl(@PathVariable String url) {
        Optional<Poll> poll = pollService.findPollByUrl(url);

        if (poll.isPresent()) {
            return ResponseEntity.ok(poll.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("pin")
    public ResponseEntity<Poll> findPollByPin(@PathVariable int pin) {
        Optional<Poll> poll = pollService.findPollByPin(pin);

        if (poll.isPresent()) {
            return ResponseEntity.ok(poll.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/owner/{username}")
    public List<Poll> findByPollsByOwnerUsername(@PathVariable String username) {
        return pollService.findPollsByOwnerUsername(username);
    }

    @GetMapping("/not-passed-end-time")
    public List<Poll> findPollsNotPassedEndTime() {
        return pollService.findPollsNotPassedEndTime();
    }

    @GetMapping("/passed-end-time")
    public List<Poll> findPollsPassedEndTime() {
        return pollService.findPollsPassedEndTime();
    }

    @GetMapping("/public")
    public List<Poll> findPublicPolls(@RequestParam("privacy") PollPrivacy privacy) {
        return pollService.findPublicPolls(PollPrivacy.PUBLIC);
    }

    @GetMapping("/all")
    public List<Poll> getAllPolls() {
        return pollService.getAllPolls();
    }

    // UPDATE
    @PutMapping
    public ResponseEntity<?> updatePoll(@RequestBody Poll poll) {
        try {
            Poll updatedPoll = pollService.updatePoll(poll);
            return ResponseEntity.ok(updatedPoll);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE
    @DeleteMapping("{pollId}")
    public ResponseEntity<Poll> deletePollById(@PathVariable("pollId") Long pollId) {
        try {
            pollService.deletePollById(pollId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}