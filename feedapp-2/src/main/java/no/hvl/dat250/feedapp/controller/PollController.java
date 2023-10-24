package no.hvl.dat250.feedapp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.feedapp.DTO.PollDTO;
import no.hvl.dat250.feedapp.exception.BadRequestException;
import no.hvl.dat250.feedapp.exception.ResourceNotFoundException;
import no.hvl.dat250.feedapp.model.jpa.PollPrivacy;
import no.hvl.dat250.feedapp.model.jpa.Account;
import no.hvl.dat250.feedapp.model.jpa.Poll;
import no.hvl.dat250.feedapp.model.jpa.Vote;
import no.hvl.dat250.feedapp.service.jpa.PollService;
import no.hvl.dat250.feedapp.service.mongo.PollResultService;

@RestController
@RequestMapping("/poll") // All request mappings start with poll
public class PollController {

    // The Controller layer communicates with the service layer
    PollService pollService;

    PollResultService pollResultService;

    public PollController(PollService pollService) {
        this.pollService = pollService;
    }
    
    
    // CREATE
    @PostMapping
    public ResponseEntity<?> createPoll(@RequestBody PollDTO poll, UsernamePasswordAuthenticationToken token) {
        try {
            Account user = (Account) token.getPrincipal();
            Poll createdPoll = pollService.createPoll(poll, user);
            PollDTO pollDTO = pollToPollDTO(createdPoll);
            pollResultService.savePollResult(pollDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(pollDTO);
        } catch (BadRequestException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // READ
    @GetMapping("{pollId}")
    public ResponseEntity<?> findPollById(@PathVariable("pollId") Long pollId) {
        try {
            Poll poll = pollService.findPollById(pollId);
            return ResponseEntity.ok(pollToPollDTO(poll));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
      

        // return ResponseHandler.responseBuilder(
        //     "Requested Poll Details are given here.",
        //     HttpStatus.OK,
        //     pollService.getPoll(pollId));
    }

    @GetMapping("/url")
    public ResponseEntity<?> findPollByUrl(@PathVariable String url) {
        try {
            Poll poll = pollService.findPollByUrl(url);
            return ResponseEntity.ok(pollToPollDTO(poll));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/pin/{id}")
    public ResponseEntity<?> findPollByPin(@PathVariable("id") int pin) {
        try {
            Poll poll = pollService.findPollByPin(pin);
            return ResponseEntity.ok(pollToPollDTO(poll));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/owner/{username}")
    public ResponseEntity<?> findByPollsByOwnerUsername(@PathVariable String username) {
        try{
            List<Poll> polls = pollService.findPollsByOwnerUsername(username);
            List<PollDTO> pollDTOs = polls.stream().map(poll -> pollToPollDTO(poll)).toList();
            return ResponseEntity.ok(pollDTOs);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/active")
    public ResponseEntity<?> findPollsNotPassedEndTime() {
        try {
            List<Poll> polls = pollService.findPollsNotPassedEndTime();
            List<PollDTO> pollDTOs = polls.stream().map(poll -> pollToPollDTO(poll)).toList();
            return ResponseEntity.ok(pollDTOs);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/ended")
    public ResponseEntity<?> findPollsPassedEndTime() {
        try {
            List<Poll> polls = pollService.findPollsPassedEndTime();
            List<PollDTO> pollDTOs = polls.stream().map(poll -> pollToPollDTO(poll)).toList();
            return ResponseEntity.ok(pollDTOs);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/public")
    public ResponseEntity<?> findPublicPolls() {
       try {
            List<Poll> polls = pollService.findPublicPolls(PollPrivacy.PUBLIC);
            List<PollDTO> pollDTOs = polls.stream().map(poll -> pollToPollDTO(poll)).toList();
            return ResponseEntity.ok(pollDTOs);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
       }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllPolls() {
        try {
            List<Poll> polls = pollService.getAllPolls();
            List<PollDTO> pollDTOs = polls.stream().map(poll -> pollToPollDTO(poll)).toList();
            return ResponseEntity.ok(pollDTOs);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    // UPDATE
    @PutMapping
    public ResponseEntity<?> updatePoll(@RequestBody Poll poll) {
        try {
            Poll updatedPoll = pollService.updatePoll(poll);
            return ResponseEntity.ok(pollToPollDTO(updatedPoll));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    // DELETE
    @DeleteMapping("{pollId}")
    public ResponseEntity<?> deletePollById(@PathVariable("pollId") Long pollId) {
        try {
            pollService.deletePollById(pollId);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    private PollDTO pollToPollDTO(Poll poll) {

        PollDTO pollDTO = new PollDTO();
        pollDTO.id = poll.getId();
        pollDTO.pollUrl = poll.getPollURL();
        pollDTO.pollPin = poll.getPollPin();
        pollDTO.question = poll.getQuestion();
        pollDTO.startTime = poll.getStartTime();
        pollDTO.endTime = poll.getEndTime();
        pollDTO.pollPrivacy = poll.getPollPrivacy();
        pollDTO.pollOwner = poll.getAccount().getUsername();
        pollDTO.pollOwnerId = poll.getAccount().getId();
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