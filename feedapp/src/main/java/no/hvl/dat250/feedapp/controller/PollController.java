package no.hvl.dat250.feedapp.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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

import no.hvl.dat250.feedapp.Account;
import no.hvl.dat250.feedapp.Poll;
import no.hvl.dat250.feedapp.PollPrivacy;
import no.hvl.dat250.feedapp.Vote;
import no.hvl.dat250.feedapp.DTO.PollDTO;
import no.hvl.dat250.feedapp.repositories.AccountRepository;
import no.hvl.dat250.feedapp.repositories.PollRepository;

@RestController
@RequestMapping("/poll")
public class PollController {

    @Autowired
    private PollRepository pollRepository;
    @Autowired
    private AccountRepository accountRepository;

    @PostMapping("")
    public ResponseEntity<?> createPoll(@RequestBody PollDTO pollDTO) {
        try {
            Poll poll = new Poll();
            poll = pollRepository.save(poll);
            Account account = accountRepository.findById(pollDTO.pollOwnerId)
                .orElseThrow(() -> new RuntimeException("Account with id " + pollDTO.pollOwnerId + " not found"));
            poll.setPollPin((poll.getId().intValue()));
            poll.setPollUrl("https://feedapp.no/poll/" + poll.getId().toString());
            poll.setQuestion(pollDTO.question);
            poll.setEndTime(pollDTO.endTime);
            poll.setStartTime(pollDTO.startTime);
            poll.setPollPrivacy(pollDTO.privacy);
            
            poll.setPollOwner(account);
            account.getPolls().add(poll);
            pollRepository.save(poll);
            return ResponseEntity.ok(pollToPollDTO(poll));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(e.getMessage());
        }
    }

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

    @GetMapping("/account")
    public ResponseEntity<?>  getPollByUsername(@RequestParam(value = "username", required = true) String username) {
        try {
            List<Poll> polls = pollRepository.findByPollOwnerUsername(username);
            List<PollDTO> pollDTOs = polls.stream()
                .map(this::pollToPollDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(pollDTOs);
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

    @GetMapping("/active")
     public ResponseEntity<?> getPollsNotEnded() {
        try {
            List<Poll> activePolls = pollRepository.findPollsNotPassedEndTime();
            
            // Convert Poll entities to PollDTOs
            List<PollDTO> activePollDTOs = activePolls.stream()
                    .map(this::pollToPollDTO)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(activePollDTOs);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        }
    }

    @GetMapping("/ended")
     public ResponseEntity<?> getPollsEnded() {
        try {
            List<Poll> activePolls = pollRepository.findPollsPassedEndTime();
            
            // Convert Poll entities to PollDTOs
            List<PollDTO> activePollDTOs = activePolls.stream()
                    .map(this::pollToPollDTO)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(activePollDTOs);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        }
    }

    @GetMapping("/public")
        public ResponseEntity<?> getPublicPolls() {
            try {
                List<Poll> activePolls = pollRepository.findPublicPolls(PollPrivacy.PUBLIC);
                
                // Convert Poll entities to PollDTOs
                List<PollDTO> activePollDTOs = activePolls.stream()
                        .map(this::pollToPollDTO)
                        .collect(Collectors.toList());
                
                return ResponseEntity.ok(activePollDTOs);
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

    @PutMapping("/{id}/endtime")
    public ResponseEntity<?> updateEndTime(@PathVariable(value = "id", required = true) String id, @RequestParam(value = "time", required = true) String endTime) {
        try {
            Poll poll = pollRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new RuntimeException("Poll with id " + id + " not found"));
            poll.setEndTime(LocalDateTime.parse(endTime));
            pollRepository.save(poll);
            return ResponseEntity.ok(pollToPollDTO(poll));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        }
    }

    @PutMapping("/{id}/privacy")
    public ResponseEntity<?> updatePrivacy(@PathVariable(value = "id", required = true) String id, @RequestParam(value = "privacy", required = true) String privacy) {
        try {
            Poll poll = pollRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new RuntimeException("Poll with id " + id + " not found"));
            poll.setPollPrivacy(PollPrivacy.valueOf(privacy));
            pollRepository.save(poll);
            return ResponseEntity.ok(pollToPollDTO(poll));
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
        pollDTO.startTime = poll.getStartTime();
        pollDTO.endTime = poll.getEndTime();
        pollDTO.privacy = poll.getPollPrivacy();
        pollDTO.pollOwner = poll.getPollOwner().getUsername();
        pollDTO.pollOwnerId = poll.getPollOwner().getId();
        pollDTO.totalVotes = poll.getVotes().size();
        pollDTO.yesVotes = 0;
        pollDTO.noVotes = 0;
        for (Vote vote : poll.getVotes()) {
            if (vote.isAnswer() == false) {
                pollDTO.noVotes++;
            } else {
                pollDTO.yesVotes++;
            }
        }

        return pollDTO;
    }

}
