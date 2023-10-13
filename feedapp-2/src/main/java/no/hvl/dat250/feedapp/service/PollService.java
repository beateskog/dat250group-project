package no.hvl.dat250.feedapp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import no.hvl.dat250.feedapp.model.Poll;
import no.hvl.dat250.feedapp.model.PollPrivacy;

public interface PollService {

    // CREATE
    public String createPoll(Poll poll);
    public String createPoll(String question, LocalDateTime start, LocalDateTime end);

    // READ
    public Poll findPollById(Long pollId);
    public Optional<Poll> findPollByUrl(String url);
    public Optional<Poll> findPollByPin(int pin);
    public List<Poll> findPollsByOwnerUsername(String username);
    public List<Poll> findPollsNotPassedEndTime();
    public List<Poll> findPollsPassedEndTime();
    public List<Poll> findPublicPolls(PollPrivacy privacy);
    public List<Poll> getAllPolls();

    // UPDATE
    public Poll updatePoll(Poll poll);

    // DELETE
    public String deletePollById(Long pollId);
      
}
