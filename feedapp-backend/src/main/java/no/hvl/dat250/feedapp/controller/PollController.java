package no.hvl.dat250.feedapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.feedapp.dto.PollDTO;
import no.hvl.dat250.feedapp.exception.AccessDeniedException;
import no.hvl.dat250.feedapp.exception.BadRequestException;
import no.hvl.dat250.feedapp.exception.ResourceNotFoundException;
import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.model.Poll;
import no.hvl.dat250.feedapp.service.PollService;

@RestController
@RequestMapping("/poll") // All request mappings start with poll
@CrossOrigin(origins = "http://localhost:4200")
public class PollController {

    // The Controller layer communicates with the service layer
    @Autowired
    private PollService pollService;

    // CREATE
    @PostMapping
    public ResponseEntity<?> createPoll(@RequestBody PollDTO poll, UsernamePasswordAuthenticationToken token) {
        try {
            Account user = (Account) token.getPrincipal();
            Poll createdPoll = pollService.createPoll(poll, user);
            PollDTO pollDTO = PollDTO.pollToPollDTO(createdPoll);
            return ResponseEntity.status(HttpStatus.CREATED).body(pollDTO);
        } catch (BadRequestException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // READ
    @GetMapping("{pollId}")
    public ResponseEntity<?> findPollById(@PathVariable("pollId") Long pollId) {
        try {
            Poll poll = pollService.findPollById(pollId);
            return ResponseEntity.ok(PollDTO.pollToPollDTO(poll));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

    }

    @GetMapping("/url")
    public ResponseEntity<?> findPollByUrl(@PathVariable String url) {
        try {
            Poll poll = pollService.findPollByUrl(url);
            return ResponseEntity.ok(PollDTO.pollToPollDTO(poll));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/pin/{id}")
    public ResponseEntity<?> findPollByPin(@PathVariable("id") int pin) {
        try {
            Poll poll = pollService.findPollByPin(pin);
            return ResponseEntity.ok(PollDTO.pollToPollDTO(poll));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/owner")
    public ResponseEntity<?> findByPollsByOwnerUsername(UsernamePasswordAuthenticationToken token) {
        try{
            Account user = (Account) token.getPrincipal();
            String username = user.getUsername();
            List<Poll> polls = pollService.findPollsByOwnerUsername(username);
            List<PollDTO> pollDTOs = polls.stream().map(poll -> PollDTO.pollToPollDTO(poll)).toList();
            return ResponseEntity.ok(pollDTOs);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/active")
    public ResponseEntity<?> findAllPollsNotPassedEndTime(UsernamePasswordAuthenticationToken token) {
        try {
            Account user = (Account) token.getPrincipal();
            if (user == null) {
                throw new AccessDeniedException("You are not authorized to view this poll");
            }
            List<Poll> polls = pollService.findAllPollsNotPassedEndTime();
            List<PollDTO> pollDTOs = polls.stream().map(poll -> PollDTO.pollToPollDTO(poll)).toList();
            return ResponseEntity.ok(pollDTOs);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/ended")
    public ResponseEntity<?> findAllPollsPassedEndTime(UsernamePasswordAuthenticationToken token) {
        try {
            Account user = (Account) token.getPrincipal();
            if (user == null) {
                throw new AccessDeniedException("You are not authorized to view this poll");
            }
            List<Poll> polls = pollService.findAllPollsPassedEndTime();
            List<PollDTO> pollDTOs = polls.stream().map(poll -> PollDTO.pollToPollDTO(poll)).toList();
            return ResponseEntity.ok(pollDTOs);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/active/public")
    public ResponseEntity<?> findPublicPollsNotPassedEndTime() {
        try {
            List<Poll> polls = pollService.findPublicPollsNotPassedEndTime();
            List<PollDTO> pollDTOs = polls.stream().map(poll -> PollDTO.pollToPollDTO(poll)).toList();
            return ResponseEntity.ok(pollDTOs);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/ended/public")
    public ResponseEntity<?> findPublicPollsPassedEndTime() {
        try {
            List<Poll> polls = pollService.findPublicPollsPassedEndTime();
            List<PollDTO> pollDTOs = polls.stream().map(poll -> PollDTO.pollToPollDTO(poll)).toList();
            return ResponseEntity.ok(pollDTOs);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/all/public")
    public ResponseEntity<?> findPublicPolls() {
       try {
            List<Poll> polls = pollService.findAllPublicPolls();
            List<PollDTO> pollDTOs = polls.stream().map(poll -> PollDTO.pollToPollDTO(poll)).toList();
            return ResponseEntity.ok(pollDTOs);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
       } catch (Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllPolls(UsernamePasswordAuthenticationToken token) {
        try {
            Account user = (Account) token.getPrincipal();
            if (user == null) {
                throw new AccessDeniedException("You are not authorized to view this poll");
            }
            List<Poll> polls = pollService.findAllPolls();
            List<PollDTO> pollDTOs = polls.stream().map(poll -> PollDTO.pollToPollDTO(poll)).toList();
            return ResponseEntity.ok(pollDTOs);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        } 
    }

    // UPDATE
    @PutMapping
    public ResponseEntity<?> updatePoll(UsernamePasswordAuthenticationToken token,@RequestBody PollDTO poll) {
        try {
            Account user = (Account) token.getPrincipal();
            if (!user.getId().equals(poll.getPollOwnerId())) {
                throw new AccessDeniedException("You are not authorized to update this poll");
            }
            Poll pollToUpdate = pollService.findPollById(poll.getId());
            if (!user.getId().equals(pollToUpdate.getAccount().getId())) {
                throw new AccessDeniedException("You are not authorized to update this poll");
            }
            Poll updatedPoll = pollService.updatePoll(poll);
            return ResponseEntity.ok(PollDTO.pollToPollDTO(updatedPoll));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // DELETE
    @DeleteMapping("{pollId}")
    public ResponseEntity<?> deletePollById(UsernamePasswordAuthenticationToken token,@PathVariable("pollId") Long pollId) {
        try {
            Account user = (Account) token.getPrincipal();
        
            String resp = pollService.deletePollById(pollId, user);
            return ResponseEntity.ok().body(resp);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    
}