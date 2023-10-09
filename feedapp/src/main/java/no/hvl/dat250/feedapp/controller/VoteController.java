package no.hvl.dat250.feedapp.controller;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.feedapp.Account;
import no.hvl.dat250.feedapp.Poll;
import no.hvl.dat250.feedapp.Role;
import no.hvl.dat250.feedapp.Vote;
import no.hvl.dat250.feedapp.VotingPlatform;
import no.hvl.dat250.feedapp.DTO.IoTDTO;
import no.hvl.dat250.feedapp.DTO.VoteDTO;
import no.hvl.dat250.feedapp.repositories.AccountRepository;
import no.hvl.dat250.feedapp.repositories.PollRepository;
import no.hvl.dat250.feedapp.repositories.VoteRepository;

@RestController
public class VoteController {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private AccountRepository accountRepository;

    @PostMapping("poll/{id}/vote")
    public ResponseEntity<?> createVote(@PathVariable String id, @RequestBody VoteDTO voteDTO) {
        try {
            Vote vote = new Vote();
            vote = voteRepository.save(vote);
            vote.setAnswer(voteDTO.vote);
            vote.setVoteTime(LocalDateTime.now());
            Poll poll = pollRepository.findById(Long.parseLong(id))
                    .orElseThrow(() -> new RuntimeException("Poll with id " + id + " not found"));
            if (voteDTO.voterId != null){
                Account account = accountRepository.findById(voteDTO.voterId)
                    .orElseThrow(() -> new RuntimeException("Account with id " + voteDTO.voterId + " not found"));
                vote.setAccount(account);
                account.getVotes().add(vote);
            }
            vote.setPoll(poll);
            poll.getVotes().add(vote);
            voteRepository.save(vote);
            return ResponseEntity.ok(voteToVoteDTO(vote));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/IoTvotes")
    public ResponseEntity<?> getIoTVotes(@RequestBody IoTDTO ioTDTO) {
        try {
            Poll poll = pollRepository.findById(ioTDTO.pollId)
                    .orElseThrow(() -> new RuntimeException("Poll with id " + ioTDTO.pollId + " not found"));
            int yesVotes = ioTDTO.yesVotes;
            int noVotes = ioTDTO.noVotes;
            while (yesVotes > 0) {
                Vote vote = new Vote();
                vote = voteRepository.save(vote);
                vote.setAnswer(true);
                vote.setVoteTime(LocalDateTime.now());
                vote.setVotingPlatform(VotingPlatform.IoT);
                poll.getVotes().add(vote);
                vote.setPoll(poll);
                yesVotes--;
                pollRepository.save(poll);
            }
            while (noVotes > 0) {
                Vote vote = new Vote();
                vote = voteRepository.save(vote);
                vote.setAnswer(false);
                vote.setVoteTime(LocalDateTime.now());
                vote.setVotingPlatform(VotingPlatform.IoT);
                vote.setPoll(poll);
                poll.getVotes().add(vote);
                noVotes--;
                pollRepository.save(poll);
            }
            pollRepository.save(poll);

            return ResponseEntity.ok(ioTDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

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

    @GetMapping("vote/{id}")
    public ResponseEntity<?> getVotesById(@PathVariable Long id) {
        try {
            Vote vote = voteRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Poll with id " + id + " not found"));
            return ResponseEntity.ok(voteToVoteDTO(vote));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("vote/{id}")
    public ResponseEntity<?> updateVote(@PathVariable String id) {
        try {
            Vote vote = voteRepository.findById(Long.parseLong(id))
                    .orElseThrow(() -> new RuntimeException("Vote with id " + id + " not found"));
            vote.setAnswer(!vote.isAnswer());
            voteRepository.save(vote);
            return ResponseEntity.ok(voteToVoteDTO(vote));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    public VoteDTO voteToVoteDTO(Vote vote) {

        VoteDTO voteDTO = new VoteDTO();
        voteDTO.id = vote.getId();
        if (vote.getAccount() == null) {
            voteDTO.voterId = null;
            voteDTO.voter = Role.ANONYMOUS.toString();
        } else {
            voteDTO.voterId = vote.getAccount().getId();
            voteDTO.voter = vote.getAccount().getRole().toString();
        }
        voteDTO.vote = vote.isAnswer();
        voteDTO.pollId = vote.getPoll().getId();
        voteDTO.votingPlatform = vote.getVotingPlatform();
        

        return voteDTO;
    }
        
}

    


    

