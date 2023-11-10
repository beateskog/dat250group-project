package no.hvl.dat250.feedapp.service.jpa.implementation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat250.feedapp.dto.PollDTO;
import no.hvl.dat250.feedapp.exception.ResourceNotFoundException;
import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.model.Poll;
import no.hvl.dat250.feedapp.model.Role;
import no.hvl.dat250.feedapp.model.messaging.PollClosedEvent;
import no.hvl.dat250.feedapp.model.messaging.PollOpenedEvent;
import no.hvl.dat250.feedapp.repository.PollRepository;
import no.hvl.dat250.feedapp.service.PollService;
import no.hvl.dat250.feedapp.service.messaging.DweetService;
import no.hvl.dat250.feedapp.service.messaging.MessagingService;


@Service
public class PollServiceImpl implements PollService {
    
    @Autowired
    private DweetService dweetService;

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private MessagingService messagingService;

    // -------------------------------------------------- CREATE -------------------------------------------------------
    @Override
    public Poll createPoll(PollDTO pollDTO, Account account) {
        if (pollDTO.getQuestion() == null) {
            throw new IllegalArgumentException("Question cannot be null");
        }
        if (pollDTO.getStartTime() == null) {
            throw new IllegalArgumentException("Start time cannot be null");
        }
        if (pollDTO.getEndTime() == null) {
            throw new IllegalArgumentException("End time cannot be null");
        }
        if (pollDTO.getPollPrivacy() == null) {
            throw new IllegalArgumentException("Poll privacy cannot be null");
        }
        
        Poll poll = new Poll();
    
        poll.setQuestion(pollDTO.getQuestion());
        poll.setStartTime(pollDTO.getStartTime());
        poll.setEndTime(pollDTO.getEndTime());
        poll.setPollPrivacy(pollDTO.getPollPrivacy());
        poll.setAccount(account);
        pollRepository.save(poll);

        int uniquePin = poll.getId().intValue();
        String uniqueUrl = "http://localhost:8080/poll/" + uniquePin;
        poll.setPollURL(uniqueUrl);
        poll.setPollPin(uniquePin);
        
        //messagingService.sendMessageToQueue("pollResultQueue", poll);
        if (poll.getStartTime().toLocalDate() == LocalDate.now()){
            dweetService.postPollOpenedEvent(new PollOpenedEvent(poll.getId().toString(), poll.getQuestion(), poll.getStartTime().toString()));

        }
        pollRepository.save(poll);
        return poll;
    }


    // --------------------------------------------------- READ --------------------------------------------------------
    @Override
    public Poll findPollById(Long pollId) {
        if (pollRepository.findById(pollId).isEmpty()) {
            throw new ResourceNotFoundException("Requested poll with ID: " + pollId + " does not exist.");
        }
        return pollRepository.findById(pollId).get();
    }
 
    @Override
    public Poll findPollByUrl(String url) {
        Poll poll = pollRepository.findPollByPollURL(url)
            .orElseThrow(() -> new ResourceNotFoundException("A poll with the given url: " + url + " does not exist."));
        return poll;
    }

    @Override
    public Poll findPollByPin(int pin) {
        Poll poll = pollRepository.findPollByPollPin(pin)
            .orElseThrow(() -> new ResourceNotFoundException("A poll with the given pin: " + pin + " does not exist."));
        return poll;
    }

    @Override
    public List<Poll> findPollsByOwnerUsername(String username) {
        List<Poll> polls = pollRepository.findPollsByOwnerUsername(username);

        return polls;
    }

    @Override
    public List<Poll> findAllPollsNotPassedEndTime() {
        List<Poll> polls = pollRepository.findAllPollsNotPassedEndTime();
        return polls;
    }

    @Override
    public List<Poll> findAllPollsPassedEndTime() {
        List<Poll> polls = pollRepository.findAllPollsPassedEndTime();
        
        return polls;
    }

    // -------------------------------------------------- UPDATE -------------------------------------------------------

    @Override
    public Poll updatePoll(PollDTO poll) {
        // Retrieve the existing poll from the repository
        if (poll.getId() == null) {
            throw new IllegalArgumentException("Poll ID must be provided.");
        }
        
        Optional<Poll> existingPollOptional = pollRepository.findById(poll.getId());

        if (existingPollOptional.isEmpty()) {
            throw new ResourceNotFoundException("Poll with ID " + poll.getId() + " does not exist.");
        }

        Poll existingPoll = existingPollOptional.get();

        // Update the start and end times if provided
        if (poll.getStartTime() != null) {
            if (poll.getStartTime().isAfter(existingPoll.getEndTime())) {
                throw new IllegalArgumentException("Start time cannot be after end time.");
            }
            if (poll.getStartTime().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Start time cannot be before current time.");
            }
            existingPoll.setStartTime(poll.getStartTime());
        }

        if (poll.getEndTime() != null) {
            if (poll.getEndTime().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("End time cannot be before current time.");
            }
            if (poll.getEndTime().isBefore(existingPoll.getStartTime())) {
                throw new IllegalArgumentException("End time cannot be before start time.");
            }
            existingPoll.setEndTime(poll.getEndTime());
        }

        // Update the privacy status if provided
        if (poll.getPollPrivacy() != null) {
            existingPoll.setPollPrivacy(poll.getPollPrivacy());
        }

        // Save the updated poll in the repository
        pollRepository.save(existingPoll);

        return existingPoll; // Return the updated poll
    }

    // -------------------------------------------------- DELETE -------------------------------------------------------
    @Override
    public String deletePollById(Long pollId, Account user) {
        if (user.getRole() == Role.ADMIN) {
            pollRepository.deleteById(pollId);
            return "Poll with pollId: " + pollId + " has successfully been deleted.";
        }
        Poll poll = pollRepository.findById(pollId) 
            .orElseThrow(() -> new ResourceNotFoundException("A poll with the given ID: " + pollId + " does not exist."));
        if (poll.getAccount().getId() == user.getId()) {
            pollRepository.deleteById(pollId);
            return "Poll with pollId: " + pollId + " has successfully been deleted.";
        }
        throw new IllegalArgumentException("You do not have permission to delete this poll."); 
    }

    @Override
    public List<Poll> findAllPolls() {
        List<Poll> polls = pollRepository.findAll();
        return polls;
    }

    @Override
    public List<Poll> findPublicPollsNotPassedEndTime() {
       List<Poll> polls = pollRepository.findPublicPollsNotPassedEndTime();
       return polls;
    }

    @Override
    public List<Poll> findPublicPollsPassedEndTime() {
        List<Poll> polls = pollRepository.findPublicPollsPassedEndTime();
        return polls;
        }

    @Override
    public List<Poll> findAllPublicPolls() {
        List<Poll> polls = pollRepository.findAllPublicPolls();
        return polls;
    }

    @Override
    public void dweetPollsEndToday() {
        List<Poll> endedPolls = pollRepository.findPollsEndToday();
        for (Poll poll : endedPolls) {
            PollClosedEvent pollClosedEvent = new PollClosedEvent(poll.getId().toString(), poll.getQuestion(), poll.getEndTime().toString());
            pollClosedEvent.setPollId(poll.getId().toString());
            pollClosedEvent.setPollQuestion(poll.getQuestion());
            pollClosedEvent.setEndTime(poll.getEndTime().toString());
            dweetService.postPollClosedEvent(pollClosedEvent);
        }
    }

    @Override
    public void dweetPollsOpenToday() {
        List<Poll> openedPolls = pollRepository.findPollsOpenToday();
        for (Poll poll : openedPolls) {
            PollOpenedEvent pollOpenedEvent = new PollOpenedEvent(poll.getId().toString(), poll.getQuestion(), poll.getStartTime().toString());
            pollOpenedEvent.setPollId(poll.getId().toString());
            pollOpenedEvent.setPollQuestion(poll.getQuestion());
            pollOpenedEvent.setStartTime(poll.getStartTime().toString());
            dweetService.postPollOpenedEvent(pollOpenedEvent);
        }
    }

    @Override
    public void savePollsEndToday(){
        List<Poll> polls = pollRepository.findPollsEndToday();
        List<PollDTO> endedPollsDTO = polls.stream().map(poll -> PollDTO.pollToPollDTO(poll)).toList();
        for (PollDTO pollDTO : endedPollsDTO) {
            messagingService.sendMessageToQueue("pollResultsExchange", "", pollDTO);
        }
    }


    @Override
    public Poll findPublicPollById(Long pollId) {
        if (pollRepository.findPublicPollById(pollId).isEmpty()) {
            throw new ResourceNotFoundException("No public polls with ID: " + pollId);
        }
        return pollRepository.findById(pollId).get();
    }
    
}
