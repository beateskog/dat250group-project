package no.hvl.dat250.feedapp.service;

import java.time.LocalDateTime;
import java.util.List;

import no.hvl.dat250.feedapp.DTO.PollDTO;
import no.hvl.dat250.feedapp.model.Poll;
import no.hvl.dat250.feedapp.model.PollPrivacy;


public interface PollService {

    // CREATE
    public Poll createPoll(PollDTO poll);
    public Poll createPoll(String question, LocalDateTime start, LocalDateTime end);

    // READ
    public Poll findPollById(Long pollId);
    public Poll findPollByUrl(String url);
    public Poll findPollByPin(int pin);
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
