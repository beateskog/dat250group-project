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
import no.hvl.dat250.feedapp.service.mongo.PollResultService;

/**
 * The PollController class is responsible for handling requests from the client related to the Poll model.
 */
@RestController
@RequestMapping("/poll") 
@CrossOrigin(origins = "http://localhost:4200")
public class PollController {

    @Autowired
    private PollService pollService;

    @Autowired
    private PollResultService pollResultService;

    // CREATE
    /**
     * Creates a new poll.
     * @param poll The PollDTO object that contains the information about the poll to be created.
     * @param token The authentication token of the user creating the poll.
     * @return Returns the created poll as a PollDTO object.
     * @throws BadRequestException if there is something wrong with the request body.
     * @throws IllegalArgumentException if any of the required fields are null.
     * 
     */
    @PostMapping
    public ResponseEntity<?> createPoll(@RequestBody PollDTO poll, UsernamePasswordAuthenticationToken token) {
        try {
            Account user = (Account) token.getPrincipal();
            Poll createdPoll = pollService.createPoll(poll, user);
            PollDTO pollDTO = PollDTO.pollToPollDTO(createdPoll);
            return ResponseEntity.status(HttpStatus.CREATED).body(pollDTO);
        } catch (BadRequestException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // READ
    /**
     * Finds a poll by its ID.
     * @param pollId The ID of the poll to be found.
     * @return The found poll as a PollDTO object.
     */
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

    /**
     * Finds a poll by its URL.
     * @param url The URL of the poll to be found.
     * @return The found poll as a PollDTO object.
     */
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

    /**
     * Finds a poll by its pin.
     * @param pin The pin of the poll to be found.
     * @return The found poll as a PollDTO object.
     * @throws ResourceNotFoundException If no poll with the given pin exists.
     * @throws BadRequestException If the pin is not a valid pin.
     * @throws Exception If something unexpected happens.
     */
    @GetMapping("/pin/{id}")
    public ResponseEntity<?> findPollByPin(@PathVariable("id") int pin) {
        try {
            Poll poll = pollService.findPollByPin(pin);
            return ResponseEntity.ok(PollDTO.pollToPollDTO(poll));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (BadRequestException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        } 
    }

    /**
     * Finds all polls created by a given user based on the token
     * @param token The authentication token of the user.
     * @return A list of all polls created by the given user.
     * If no polls are found, an empty list is returned.
     * @throws Exception If something unexpected happens.
     */
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
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    /**
     * Finds all polls that have not passed their end time.
     * @param token The authentication token of the user.
     * @return A list of all polls that have not passed their end time.
     * If no polls are found, an empty list is returned.
     * @throws AccessDeniedException If the user is not authorized to view the polls.
     */
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

    /**
     * Finds all polls that have passed their end time.
     * @param token The authentication token of the user.
     * @return A list of all polls that have passed their end time.
     * If no polls are found, an empty list is returned.
     * @throws AccessDeniedException If the user is not authorized to view the polls.
     * @throws Exception If something unexpected happens.
     */
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

    /**
     * Finds all public polls that have not passed their end time.
     * @return A list of all public polls not passed their end time.
     * If no polls are found, an empty list is returned.
     * @throws Exception If something unexpected happens.
     */
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

    /**
     * Finds all public polls that have passed their end time.
     * @return A list of all public polls that have passed their end time.
     * If no polls are found, an empty list is returned.
     * @throws Exception If something unexpected happens.
     */
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

    /**
     * Finds all public polls.
     * @return A list of all public polls.
     * If no polls are found, an empty list is returned.
     * @throws Exception If something unexpected happens.
     */
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

    /**
     * Requires athentication
     * Finds all polls in the database.
     * @return A list of all polls in the database.
     * If no polls are found, an empty list is returned.
     * @throws AccessDeniedException If the user is not authorized to view the polls.
     */
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

    /**
     * Finds the result of a ended poll
     * @param pollId the id of the ended poll
     * @return the result of the poll if it exists
     */ 
    @GetMapping("/result/{pollId}")
    public ResponseEntity<?> getPollResult(@PathVariable("pollId") Long pollId) {
        try {
            return ResponseEntity.ok(pollResultService.getPollResult(pollId));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        } 
    }

    // UPDATE
    /**
     * Updates a poll.
     * @param token The authentication token of the user.
     * @param poll The PollDTO object that contains the information about the poll to be updated.
     * @return The updated poll as a PollDTO object.
     * @throws AccessDeniedException If the user is not authorized to update the poll.
     * @throws ResourceNotFoundException If no poll with the given ID exists.
     * @throws IllegalArgumentException If there is something wrong with start time, end time, or pollDTO object
     * @throws Exception If something unexpected happens.
     */
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
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // DELETE
    /**
     * Deletes a poll.
     * @param token The authentication token of the user.
     * @param pollId The ID of the poll to be deleted.
     * @return A message confirming that the poll has been deleted. 
     * @throws ResourceNotFoundException If no poll with the given ID exists.
     * @throws AccessDeniedException If the user is not authorized to delete the poll.
     * @throws Exception If something unexpected happens.
     */
    @DeleteMapping("{pollId}")
    public ResponseEntity<?> deletePollById(UsernamePasswordAuthenticationToken token,@PathVariable("pollId") Long pollId) {
        try {
            Account user = (Account) token.getPrincipal();
        
            String resp = pollService.deletePollById(pollId, user);
            return ResponseEntity.ok().body(resp);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    
}