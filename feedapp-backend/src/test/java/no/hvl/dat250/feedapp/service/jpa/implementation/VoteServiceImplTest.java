package no.hvl.dat250.feedapp.service.jpa.implementation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import no.hvl.dat250.feedapp.dto.VoteDTO;
import no.hvl.dat250.feedapp.dto.iot.IoTResponse;
import no.hvl.dat250.feedapp.exception.ResourceNotFoundException;
import no.hvl.dat250.feedapp.model.*;
import no.hvl.dat250.feedapp.repository.AccountRepository;
import no.hvl.dat250.feedapp.repository.PollRepository;
import no.hvl.dat250.feedapp.repository.VoteRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VoteServiceImplTest {

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private PollRepository pollRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private VoteServiceImpl voteService;


    @Test
    void testCreateVote_Success() {
        VoteDTO voteDTO = new VoteDTO();
        voteDTO.setVote(true);
        voteDTO.setPollId(1L);
        voteDTO.setVotingPlatform(VotingPlatform.WEB);
        voteDTO.setVoterId(1L);

        Poll poll = new Poll();
        Account account = new Account();

        when(pollRepository.findById(1L)).thenReturn(Optional.of(poll));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(voteRepository.save(any(Vote.class))).thenAnswer(i -> i.getArguments()[0]);

       
        Vote createdVote = voteService.createVote(voteDTO);

        assertThat(createdVote).isNotNull();
        assertThat(createdVote.isVote()).isEqualTo(true);
        assertThat(createdVote.getPoll()).isEqualTo(poll);
        assertThat(createdVote.getAccount()).isEqualTo(account);
        assertThat(createdVote.getPlatform()).isEqualTo(VotingPlatform.WEB);
    }

    @Test 
    void testCreateVote_PollNotFound() {
        VoteDTO voteDTO = new VoteDTO();
        voteDTO.setVote(true);
        voteDTO.setPollId(1L);
        voteDTO.setVotingPlatform(VotingPlatform.WEB);
        voteDTO.setVoterId(1L);

        when(pollRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
            .isThrownBy(() -> voteService.createVote(voteDTO))
            .withMessage("A poll with the given ID: 1 does not exist.");
    }


    @Test
    void testUpdateVote_Success() {
        VoteDTO voteDTO = new VoteDTO();
        voteDTO.setId(1L);
        voteDTO.setVote(true);

        Vote vote = new Vote();
        vote.setId(1L);
        vote.setVote(false);

        when(voteRepository.findById(1L)).thenReturn(Optional.of(vote));
        when(voteRepository.save(any(Vote.class))).thenAnswer(i -> i.getArguments()[0]);

        Vote updatedVote = voteService.updateVote(voteDTO);

        assertThat(updatedVote).isNotNull();
        assertThat(updatedVote.getId()).isEqualTo(1L);
        assertThat(updatedVote.isVote()).isEqualTo(true);
    }

    @Test
    void testUpdateVote_NotFound() {
        VoteDTO voteDTO = new VoteDTO();
        voteDTO.setId(1L);
        voteDTO.setVote(true);

        when(voteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
            .isThrownBy(() -> voteService.updateVote(voteDTO))
            .withMessage("A vote with the given ID: 1 does not exist.");
    }

    @Test
    void testDeleteVote_Success() {
        Vote vote = new Vote();

        when(voteRepository.findById(1L)).thenReturn(Optional.of(vote));

        String response = voteService.deleteVote(1L);

        assertThat(response).isEqualTo("Vote with ID: null was successfully deleted.");
        verify(voteRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteVote_NotFound() {
        when(voteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
            .isThrownBy(() -> voteService.deleteVote(1L))
            .withMessage("A vote with the given ID: 1 does not exist.");
    }

    @Test
    void testGetVoteById_Success() {
        Vote vote = new Vote();

        when(voteRepository.findById(1L)).thenReturn(Optional.of(vote));

        Vote foundVote = voteService.getVote( 1L);

        assertThat(foundVote).isNotNull();
        assertThat(foundVote).isEqualTo(vote);
    }

    @Test
    void testGetVoteById_NotFound() {
        when(voteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
            .isThrownBy(() -> voteService.getVote(1L))
            .withMessage("A vote with the given ID: 1 does not exist.");
    }

    @Test
    void testGetAllVotes_Success() {
        Vote vote1 = new Vote();
        Vote vote2 = new Vote();
        Vote vote3 = new Vote();

        when(voteRepository.findAll()).thenReturn(List.of(vote1, vote2, vote3));

        List<Vote> votes = voteService.getAllVotes();

        assertThat(votes).isNotNull();
        assertThat(votes).hasSize(3);
        assertThat(votes).contains(vote1, vote2, vote3);
    }

    @Test
    void testGetAllVotes_Empty() {
        when(voteRepository.findAll()).thenReturn(List.of());

        List<Vote> votes = voteService.getAllVotes();

        assertThat(votes).isNotNull();
        assertThat(votes).hasSize(0);
    }

    @Test
    void testCreateIoTVote_Success() {
        IoTResponse response = new IoTResponse();
        response.setPollId(1L);
        response.setYesVotes(2);
        response.setNoVotes(1);

        Poll poll = new Poll();
        poll.setId(1L);

        when(pollRepository.findById(1L)).thenReturn(Optional.of(poll));
        when(voteRepository.save(any(Vote.class))).thenAnswer(i -> i.getArguments()[0]);

        List<Vote> createdVotes = voteService.createIoTVote(response);

        assertThat(createdVotes).isNotNull();
        assertThat(createdVotes).hasSize(3);
        assertThat(createdVotes.get(0).getPoll()).isEqualTo(poll);
        assertThat(createdVotes.get(0).isVote()).isEqualTo(true);
        assertThat(createdVotes.get(1).getPoll()).isEqualTo(poll);
        assertThat(createdVotes.get(1).isVote()).isEqualTo(true);
        assertThat(createdVotes.get(2).getPoll()).isEqualTo(poll);
        assertThat(createdVotes.get(2).isVote()).isEqualTo(false);
    }

    @Test
    void testCreateIoTVote_PollNotFound() {
        IoTResponse response = new IoTResponse();
        response.setPollId(1L);
        response.setYesVotes(2);
        response.setNoVotes(1);

        when(pollRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
            .isThrownBy(() -> voteService.createIoTVote(response))
            .withMessage("A poll with the given ID: 1 does not exist.");
    }

    
}
