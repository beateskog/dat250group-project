package no.hvl.dat250.feedapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.feedapp.Poll;
import no.hvl.dat250.feedapp.repositories.PollRepository;

@RestController
public class PollController {

    @Autowired
    private PollRepository pollRepository;

    @GetMapping("/poll")
    public ResponseEntity<?>  getPollByPollPin(@RequestParam(value = "pollPin", required = true) String pollPin) {
        try {
            Poll poll = pollRepository.findByPollPin(Integer.parseInt(pollPin))
                .orElseThrow(() -> new RuntimeException("Poll with pin " + pollPin + " not found"));
            return ResponseEntity.ok(poll);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        }
    }
    
    @GetMapping("/poll/{id}")
    public ResponseEntity<?> getPollById(@PathVariable(value = "id", required = true) String id) {
        try {
            Poll poll = pollRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new RuntimeException("Poll with id " + id + " not found"));
            return ResponseEntity.ok(poll);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        }
    }

}