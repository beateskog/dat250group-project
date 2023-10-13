package no.hvl.dat250.feedapp.service.implementation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Service;

import no.hvl.dat250.feedapp.exception.ResourceNotFoundException;
import no.hvl.dat250.feedapp.model.Poll;
import no.hvl.dat250.feedapp.model.PollPrivacy;
import no.hvl.dat250.feedapp.repository.PollRepository;
import no.hvl.dat250.feedapp.service.PollService;

@Service
public class PollServiceImpl implements PollService {
    
    PollRepository pollRepository;

    public PollServiceImpl (PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    // -------------------------------------------------- CREATE -------------------------------------------------------
    @Override
    public String createPoll(Poll poll) {
        String uniqueUrl = generateUniqueUrl();
        int uniquePin = generateUniquePin();

        poll.setPollURL(uniqueUrl);
        poll.setPollPin(uniquePin);
        poll.setQuestion(poll.getQuestion());
        
        pollRepository.save(poll);
        return "Poll created with URL: " + uniqueUrl + " and PIN: " + uniquePin;
    }

    @Override
    public String createPoll(String question, LocalDateTime start, LocalDateTime end) {
        String uniqueUrl = generateUniqueUrl();
        int uniquePin = generateUniquePin();

        Poll poll = new Poll();
        poll.setPollURL(uniqueUrl);
        poll.setPollPin(uniquePin);
        poll.setQuestion(question);
        poll.setStart(start);
        poll.setEnd(end);

        pollRepository.save(poll);
        return "Poll created with URL: " + uniqueUrl + " and PIN: " + uniquePin;
    }

    private String generateUniqueUrl() {
        // Implement logic to generate a unique URL (e.g., a random string or a combination of properties)
        // Ensure the generated URL is unique in your database
        // You can use UUID.randomUUID().toString() for simplicity
        return UUID.randomUUID().toString();
    }

    private int generateUniquePin() {
        // Implement logic to generate a unique PIN (e.g., a random number)
        // Ensure the generated PIN is unique in your database
        // You can use Random or other methods for generating unique PINs
        return new Random().nextInt(10000); // Change as needed
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
    public Optional<Poll> findPollByUrl(String url) {
        Optional<Poll> poll = pollRepository.findPollByUrl(url);
        if (poll.isEmpty()) {
            throw new ResourceNotFoundException("A poll with the given url: " + url + "does not exist.");
        }
        return poll;
    }

    @Override
    public Optional<Poll> findPollByPin(int pin) {
        Optional<Poll> poll = pollRepository.findPollByPin(pin);
        if (poll.isEmpty()) {
            throw new ResourceNotFoundException("A poll with the given pin: " + pin + "does not exist.");
        }
        return poll;
    }

    @Override
    public List<Poll> findPollsByOwnerUsername(String username) {
        List<Poll> polls = pollRepository.findPollsByOwnerUsername(username);
        if (polls.isEmpty()) {
            throw new ResourceNotFoundException("No polls found for the user with username: " + username);
        }
        return polls;
    }

    @Override
    public List<Poll> findPollsNotPassedEndTime() {
        LocalDateTime currentTime = LocalDateTime.now(); // Get the current time
        List<Poll> allPolls = pollRepository.findAll(); // Retrieve all polls from the repository
        List<Poll> activePolls = new ArrayList<>();

        for (Poll poll : allPolls) {
            if (poll.getEnd() != null && poll.getEnd().isAfter(currentTime) &&
                poll.getStart() != null && poll.getStart().isBefore(currentTime)) {
                // Check if the poll has both start and end times and is currently active
                activePolls.add(poll);
            }
        }
        if (activePolls.isEmpty()) {
            throw new ResourceNotFoundException("No active polls found.");
        }
        return activePolls;
    }

    @Override
    public List<Poll> findPollsPassedEndTime() {
        LocalDateTime currentTime = LocalDateTime.now(); // Get the current time
        List<Poll> allPolls = pollRepository.findAll(); // Retrieve all polls from the repository
        List<Poll> passedEndTimePolls = new ArrayList<>();

        for (Poll poll : allPolls) {
            if (poll.getEnd() != null && poll.getEnd().isBefore(currentTime)) {
                // Check if the poll has an end time and it's before the current time
                passedEndTimePolls.add(poll);
            }
        }
        if (passedEndTimePolls.isEmpty()) {
            throw new ResourceNotFoundException("No polls found that have passed their end time.");
        }
        return passedEndTimePolls;
    }

    public List<Poll> findPublicPolls(PollPrivacy privacy) {
        if (privacy != PollPrivacy.PUBLIC) {
            throw new IllegalArgumentException("Privacy parameter must be set to PollPrivacy.PUBLIC.");
        }
    
        List<Poll> publicPolls = pollRepository.findPollsByPrivacy(PollPrivacy.PUBLIC);
    
        if (publicPolls.isEmpty()) {
            throw new ResourceNotFoundException("No public polls found.");
        }
        return publicPolls;
    }

    @Override
    public List<Poll> getAllPolls() {
        return pollRepository.findAll();
    }

    // -------------------------------------------------- UPDATE -------------------------------------------------------
    
    @Override
    public Poll updatePoll(Poll poll) {
        // Retrieve the existing poll from the repository
        Optional<Poll> existingPollOptional = pollRepository.findById(poll.getId());

        if (existingPollOptional.isEmpty()) {
            throw new ResourceNotFoundException("Poll with ID " + poll.getId() + " does not exist.");
        }

        Poll existingPoll = existingPollOptional.get();

        // Update the start and end times if provided
        if (poll.getStart() != null) {
            existingPoll.setStart(poll.getStart());
        }

        if (poll.getEnd() != null) {
            existingPoll.setEnd(poll.getEnd());
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
    public String deletePollById(Long pollId) {
        pollRepository.deleteById(pollId);
        return "Success";
    }
    
}
