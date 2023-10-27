package no.hvl.dat250.feedapp.service;

import java.time.LocalDateTime;
import java.util.List;

import no.hvl.dat250.feedapp.dto.PollDTO;
import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.model.PollPrivacy;
import no.hvl.dat250.feedapp.model.Poll;


public interface PollService {

    // CREATE
    public Poll createPoll(PollDTO poll, Account account);
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
    public Poll updatePoll(PollDTO poll);

    // DELETE
    public String deletePollById(Long pollId);
      
}
