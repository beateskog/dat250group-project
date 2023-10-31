package no.hvl.dat250.feedapp.service;

import java.util.List;

import no.hvl.dat250.feedapp.dto.PollDTO;
import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.model.Poll;


public interface PollService {

    // CREATE
    public Poll createPoll(PollDTO poll, Account account);

    // READ
    public Poll findPollById(Long pollId);
    public Poll findPollByUrl(String url);
    public Poll findPollByPin(int pin);
    public List<Poll> findPollsByOwnerUsername(String username);


    public List<Poll> findAllPollsNotPassedEndTime();
    public List<Poll> findAllPollsPassedEndTime();
    public List<Poll> findAllPolls();


    public List<Poll> findPublicPollsNotPassedEndTime();
    public List<Poll> findPublicPollsPassedEndTime();
    public List<Poll> findAllPublicPolls();
   

    // UPDATE
    public Poll updatePoll(PollDTO poll);

    // DELETE
    public String deletePollById(Long pollId, Account user);
      
}
