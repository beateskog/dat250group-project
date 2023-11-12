package no.hvl.dat250.feedapp.service.jpa.implementation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import no.hvl.dat250.feedapp.dto.PollDTO;
import no.hvl.dat250.feedapp.exception.ResourceNotFoundException;
import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.model.Poll;
import no.hvl.dat250.feedapp.model.PollPrivacy;
import no.hvl.dat250.feedapp.model.Role;
import no.hvl.dat250.feedapp.repository.PollRepository;

public class PollServiceImplTest {


    @Mock
    private PollRepository pollRepository;

    @InjectMocks
    private PollServiceImpl pollService;

    Account account;
    PollDTO pollDTO;
    Poll poll;
    Poll passedEndtimePoll;
    Poll privatePoll;

    @BeforeEach
    void setUp() {
        account = new Account(1L, "Username1", "********", Role.USER);

        pollDTO = new PollDTO();
        pollDTO.setQuestion("Do you like Christmas?");
        pollDTO.setStartTime(LocalDateTime.of(2099, 11, 10, 19, 0));
        pollDTO.setEndTime(LocalDateTime.of(2099, 12, 25, 20, 0));
        pollDTO.setPollPrivacy(PollPrivacy.PUBLIC);
        pollDTO.setPollOwnerId(1L);

        poll = new Poll();
        poll.setId(1L);
        poll.setQuestion("Do you like Christmas?");
        poll.setStartTime(LocalDateTime.of(2099, 11, 10, 19, 0));
        poll.setEndTime(LocalDateTime.of(2099, 12, 25, 20, 0));
        poll.setPollPrivacy(PollPrivacy.PUBLIC);
        poll.setAccount(account);

        passedEndtimePoll = new Poll();
        passedEndtimePoll.setId(2L);
        passedEndtimePoll.setQuestion("Do you like Easter?");
        passedEndtimePoll.setStartTime(LocalDateTime.of(2020, 11, 10, 19, 0));
        passedEndtimePoll.setEndTime(LocalDateTime.of(2020, 12, 25, 20, 0));
        passedEndtimePoll.setPollPrivacy(PollPrivacy.PUBLIC);
        passedEndtimePoll.setAccount(account);

        privatePoll = new Poll();
        privatePoll.setId(3L);
        privatePoll.setQuestion("Do you like milk?");
        privatePoll.setStartTime(LocalDateTime.of(2020, 11, 10, 19, 0));
        privatePoll.setEndTime(LocalDateTime.of(2020, 12, 25, 20, 0));
        privatePoll.setPollPrivacy(PollPrivacy.PRIVATE);
        MockitoAnnotations.openMocks(this);

    }

    @Test 
    void createPoll_Success() {
        //Need to mock the id being set by the database
        when(pollRepository.save(any(Poll.class))).thenAnswer(invocation -> {
            Poll savedPoll = invocation.getArgument(0, Poll.class);
            savedPoll.setId(1L); 
            return savedPoll;
        });
    
        Poll response = pollService.createPoll(pollDTO, account);

        assertThat(response.getId()).isEqualTo(poll.getId());
        assertThat(response.getQuestion()).isEqualTo(pollDTO.getQuestion());
        assertThat(response.getStartTime()).isEqualTo(pollDTO.getStartTime());
        assertThat(response.getEndTime()).isEqualTo(pollDTO.getEndTime());
        assertThat(response.getPollPrivacy()).isEqualTo(pollDTO.getPollPrivacy());
        assertThat(response.getAccount()).isEqualTo(account);
    }

    @Test
    void createPoll_NoQuestion() {
        pollDTO.setQuestion(null);

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> pollService.createPoll(pollDTO, account))
            .withMessage("Question cannot be null");
    }

    @Test
    void createPoll_NoStartTime() {
        pollDTO.setStartTime(null);

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> pollService.createPoll(pollDTO, account))
            .withMessage("Start time cannot be null");
    }

    @Test
    void createPoll_NoEndTime() {
        pollDTO.setEndTime(null);

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> pollService.createPoll(pollDTO, account))
            .withMessage("End time cannot be null");
    }

    @Test
    void createPoll_NoPollPrivacy() {
        pollDTO.setPollPrivacy(null);

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> pollService.createPoll(pollDTO, account))
            .withMessage("Poll privacy cannot be null");
    }

    @Test
    void findPollById_Success(){

        when(pollRepository.findById(1L)).thenReturn(java.util.Optional.of(poll));

        Poll response = pollService.findPollById(1L);

        assertThat(response.getId()).isEqualTo(poll.getId());
        assertThat(response.getQuestion()).isEqualTo(poll.getQuestion());
        assertThat(response.getStartTime()).isEqualTo(poll.getStartTime());
        assertThat(response.getEndTime()).isEqualTo(poll.getEndTime());
        assertThat(response.getPollPrivacy()).isEqualTo(poll.getPollPrivacy());
        assertThat(response.getAccount()).isEqualTo(poll.getAccount());

    }

    @Test
    void findPollById_NoPollFound(){
        when(pollRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
            .isThrownBy(() -> pollService.findPollById(1L))
            .withMessage("Requested poll with ID: 1 does not exist.");
    }

    @Test
    void findPollByUrl(){
        when(pollRepository.findPollByPollURL("poll.com")).thenReturn(Optional.of(poll));

        Poll response = pollService.findPollByUrl("poll.com");

        assertThat(response.getId()).isEqualTo(poll.getId());
        assertThat(response.getQuestion()).isEqualTo(poll.getQuestion());
        assertThat(response.getStartTime()).isEqualTo(poll.getStartTime());
        assertThat(response.getEndTime()).isEqualTo(poll.getEndTime());
        assertThat(response.getPollPrivacy()).isEqualTo(poll.getPollPrivacy());
        assertThat(response.getAccount()).isEqualTo(poll.getAccount());
    }

    @Test
    void findPollByUrl_NoPollFound(){
        when(pollRepository.findPollByPollURL("poll.com")).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
            .isThrownBy(() -> pollService.findPollByUrl("poll.com"))
            .withMessage("A poll with the given url: poll.com does not exist.");
    }

    @Test
    void findPollByPin(){
        when(pollRepository.findPollByPollPin(1)).thenReturn(Optional.of(poll));

        Poll response = pollService.findPollByPin(1);

        assertThat(response.getId()).isEqualTo(poll.getId());
        assertThat(response.getQuestion()).isEqualTo(poll.getQuestion());
        assertThat(response.getStartTime()).isEqualTo(poll.getStartTime());
        assertThat(response.getEndTime()).isEqualTo(poll.getEndTime());
        assertThat(response.getPollPrivacy()).isEqualTo(poll.getPollPrivacy());
        assertThat(response.getAccount()).isEqualTo(poll.getAccount());
    }

    @Test
    void findPollByPin_NoPollFound(){
        when(pollRepository.findPollByPollPin(1)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
            .isThrownBy(() -> pollService.findPollByPin(1))
            .withMessage("A poll with the given pin: 1 does not exist.");
    }

    @Test
    void findPollsByOwnerUsername(){
        when(pollRepository.findPollsByOwnerUsername("Username1")).thenReturn(java.util.List.of(poll));

        Poll response = pollService.findPollsByOwnerUsername("Username1").get(0);

        assertThat(response.getId()).isEqualTo(poll.getId());
        assertThat(response.getQuestion()).isEqualTo(poll.getQuestion());
        assertThat(response.getStartTime()).isEqualTo(poll.getStartTime());
        assertThat(response.getEndTime()).isEqualTo(poll.getEndTime());
        assertThat(response.getPollPrivacy()).isEqualTo(poll.getPollPrivacy());
        assertThat(response.getAccount()).isEqualTo(poll.getAccount());
    }

    @Test
    void findAllPollsNotPassedEndTime(){
        when(pollRepository.findAllPollsNotPassedEndTime()).thenReturn(java.util.List.of(poll));

        Poll response = pollService.findAllPollsNotPassedEndTime().get(0);

        assertThat(response.getId()).isEqualTo(poll.getId());
        assertThat(response.getQuestion()).isEqualTo(poll.getQuestion());
        assertThat(response.getStartTime()).isEqualTo(poll.getStartTime());
        assertThat(response.getEndTime()).isEqualTo(poll.getEndTime());
        assertThat(response.getPollPrivacy()).isEqualTo(poll.getPollPrivacy());
        assertThat(response.getAccount()).isEqualTo(poll.getAccount());
    }

    @Test
    void updatePoll(){
        pollDTO.setId(1L);
        when(pollRepository.findById(1L)).thenReturn(Optional.of(poll));
        when(pollRepository.save(any(Poll.class))).thenReturn(poll);

        Poll response = pollService.updatePoll(pollDTO);

        assertThat(response.getId()).isEqualTo(poll.getId());
        assertThat(response.getQuestion()).isEqualTo(pollDTO.getQuestion());
        assertThat(response.getStartTime()).isEqualTo(pollDTO.getStartTime());
        assertThat(response.getEndTime()).isEqualTo(pollDTO.getEndTime());
        assertThat(response.getPollPrivacy()).isEqualTo(pollDTO.getPollPrivacy());
        assertThat(response.getAccount()).isEqualTo(poll.getAccount());
    }

    @Test
    void updatePoll_NoPollId(){
        pollDTO.setId(null);

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> pollService.updatePoll(pollDTO))
            .withMessage("Poll ID must be provided.");
    }

    //THIS FEATURE HAS BEEN REMOVED
    // @Test
    // void updatePoll_StartTimeBeforeCurrentTime(){
    //     pollDTO.setId(1L);
    //     pollDTO.setStartTime(LocalDateTime.of(2010, 11, 10, 19, 0));
    //     when(pollRepository.findById(1L)).thenReturn(Optional.of(poll));

    //     assertThatExceptionOfType(IllegalArgumentException.class)
    //         .isThrownBy(() -> pollService.updatePoll(pollDTO))
    //         .withMessage("Start time cannot be before current time.");
    // }

    @Test
    void updatePoll_StartTimeAfterEndTime(){
        pollDTO.setId(1L);
        pollDTO.setStartTime(LocalDateTime.of(2100, 12, 26, 19, 0));
        when(pollRepository.findById(1L)).thenReturn(Optional.of(poll));

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> pollService.updatePoll(pollDTO))
            .withMessage("Start time cannot be after end time.");
    }

    @Test
    void updatePoll_EndTimeBeforeStartTime(){
        pollDTO.setId(1L);
        pollDTO.setStartTime(LocalDateTime.of(2099, 11, 11, 10, 0));
        pollDTO.setEndTime(LocalDateTime.of(2099, 11, 10, 19, 0));
        when(pollRepository.findById(1L)).thenReturn(Optional.of(poll));

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> pollService.updatePoll(pollDTO))
            .withMessage("End time cannot be before start time.");
    }

    @Test
    void updatePoll_EndTimeBeforeCurrentTime(){
        pollDTO.setId(1L);
        pollDTO.setEndTime(LocalDateTime.of(2023, 11, 1, 1, 0));
        when(pollRepository.findById(1L)).thenReturn(Optional.of(poll));

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> pollService.updatePoll(pollDTO))
            .withMessage("End time cannot be before current time.");
    }

    @Test
    void updatePoll_PollPrivacy(){
        pollDTO.setPollPrivacy(PollPrivacy.PRIVATE);
        pollDTO.setId(1L);
        when(pollRepository.findById(1L)).thenReturn(Optional.of(poll));
        when(pollRepository.save(any(Poll.class))).thenReturn(poll);

        pollService.updatePoll(pollDTO);

        assertThat(PollPrivacy.PRIVATE).isEqualTo(poll.getPollPrivacy());
    }

    @Test
    void deletePoll_AccountOwnerSuccess(){
        when(pollRepository.findById(1L)).thenReturn(Optional.of(poll));

        String response = pollService.deletePollById(1L, account);

        assertThat(response).isEqualTo("Poll with pollId: 1 has successfully been deleted.");
    }

    @Test
    void deletePoll_NoPollFound(){
        when(pollRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
            .isThrownBy(() -> pollService.deletePollById(1L, account))
            .withMessage("A poll with the given ID: 1 does not exist.");
    }

    @Test
    void deletePoll_NotAccountOwner(){
        when(pollRepository.findById(1L)).thenReturn(Optional.of(poll));

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> pollService.deletePollById(1L, new Account()))
            .withMessage("You do not have permission to delete this poll.");
    }

    @Test
    void deletePoll_AdminSuccess(){
        when(pollRepository.findById(1L)).thenReturn(Optional.of(poll));

        String response = pollService.deletePollById(1L, new Account(1L, "Username1", "********", Role.ADMIN));

        assertThat(response).isEqualTo("Poll with pollId: 1 has successfully been deleted.");
    }

    @Test
    void findAllPolls(){
        when(pollRepository.findAll()).thenReturn(java.util.List.of(poll));

        Poll response = pollService.findAllPolls().get(0);

        assertThat(response.getId()).isEqualTo(poll.getId());
        assertThat(response.getQuestion()).isEqualTo(poll.getQuestion());
        assertThat(response.getStartTime()).isEqualTo(poll.getStartTime());
        assertThat(response.getEndTime()).isEqualTo(poll.getEndTime());
        assertThat(response.getPollPrivacy()).isEqualTo(poll.getPollPrivacy());
        assertThat(response.getAccount()).isEqualTo(poll.getAccount());
    }

    @Test
    void findPublicPollsNotPassedEndTime(){
        when(pollRepository.findPublicPollsNotPassedEndTime()).thenReturn(java.util.List.of(poll));

        Poll response = pollService.findPublicPollsNotPassedEndTime().get(0);

        assertThat(response.getId()).isEqualTo(poll.getId());
        assertThat(response.getQuestion()).isEqualTo(poll.getQuestion());
        assertThat(response.getStartTime()).isEqualTo(poll.getStartTime());
        assertThat(response.getEndTime()).isEqualTo(poll.getEndTime());
        assertThat(response.getPollPrivacy()).isEqualTo(poll.getPollPrivacy());
        assertThat(response.getAccount()).isEqualTo(poll.getAccount());
    }

    @Test
    void findPublicPollsPassedEndTime(){
        when(pollRepository.findPublicPollsPassedEndTime()).thenReturn(java.util.List.of(passedEndtimePoll));

        Poll response = pollService.findPublicPollsPassedEndTime().get(0);

        assertThat(response.getId()).isEqualTo(passedEndtimePoll.getId());
        assertThat(response.getQuestion()).isEqualTo(passedEndtimePoll.getQuestion());
        assertThat(response.getStartTime()).isEqualTo(passedEndtimePoll.getStartTime());
        assertThat(response.getEndTime()).isEqualTo(passedEndtimePoll.getEndTime());
        assertThat(response.getPollPrivacy()).isEqualTo(passedEndtimePoll.getPollPrivacy());
        assertThat(response.getAccount()).isEqualTo(passedEndtimePoll.getAccount());
    }

    @Test
    void findAllPublicPolls(){
        when(pollRepository.findAllPublicPolls()).thenReturn(java.util.List.of(poll, passedEndtimePoll));

        Poll response = pollService.findAllPublicPolls().get(0);

        assertThat(response.getId()).isEqualTo(poll.getId());
        assertThat(response.getQuestion()).isEqualTo(poll.getQuestion());
        assertThat(response.getStartTime()).isEqualTo(poll.getStartTime());
        assertThat(response.getEndTime()).isEqualTo(poll.getEndTime());
        assertThat(response.getPollPrivacy()).isEqualTo(poll.getPollPrivacy());
        assertThat(response.getAccount()).isEqualTo(poll.getAccount());

        Poll response2 = pollService.findAllPublicPolls().get(1);

        assertThat(response2.getId()).isEqualTo(passedEndtimePoll.getId());
        assertThat(response2.getQuestion()).isEqualTo(passedEndtimePoll.getQuestion());
        assertThat(response2.getStartTime()).isEqualTo(passedEndtimePoll.getStartTime());
        assertThat(response2.getEndTime()).isEqualTo(passedEndtimePoll.getEndTime());
        assertThat(response2.getPollPrivacy()).isEqualTo(passedEndtimePoll.getPollPrivacy());
        assertThat(response2.getAccount()).isEqualTo(passedEndtimePoll.getAccount());
    }

    
}
