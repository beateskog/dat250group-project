package no.hvl.dat250.feedapp.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import no.hvl.dat250.feedapp.Poll;
import no.hvl.dat250.feedapp.Vote;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class VoteDaoTest {
    
    private static final String PERSISTENCE_UNIT_NAME = "feedapp"; 
    private EntityManagerFactory emf;
    private EntityManager em;
    private VoteDao voteDao;
    private PollDao pollDao;

    @Before
    public void setUp() {
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = emf.createEntityManager();
        voteDao = new VoteDao(em);
        pollDao = new PollDao(em);
    }

    @After
    public void tearDown() {
        em.close();
        emf.close();
    }

    @Test
    public void testCreateVote() {
        Poll poll = new Poll();
        pollDao.createPoll(poll);

        Vote vote = new Vote();
        vote.setVote(true);
        voteDao.createVote(vote, poll);

        Vote retrievedVote = voteDao.getVoteById(vote.getId());
        assertNotNull(retrievedVote);
        assertEquals(true, retrievedVote.isVote());
    }

    @Test
    public void testGetAllVotes() {
        Poll poll = new Poll();
        pollDao.createPoll(poll); 

        Vote vote1 = new Vote();
        vote1.setVote(true);
        voteDao.createVote(vote1, poll);

        Vote vote2 = new Vote();
        vote2.setVote(false);
        voteDao.createVote(vote2, poll);

        Poll poll2 = new Poll();
        pollDao.createPoll(poll2);

        Vote vote3 = new Vote();
        vote3.setVote(true);
        voteDao.createVote(vote3, poll2);

        assertEquals(2, voteDao.getAllVotesbyPollId(poll.getId()).size());
    }

    @Test
    public void testUpdateVote() {
        Poll poll = new Poll();
        pollDao.createPoll(poll);

        Vote vote = new Vote();
        vote.setVote(true);
        voteDao.createVote(vote, poll);

        vote.setVote(false);
        voteDao.updateVote(vote);

        Vote retrievedVote = voteDao.getVoteById(vote.getId());
        assertEquals(false, retrievedVote.isVote());
    }

    @Test
    public void testDeleteVote() {
        Poll poll = new Poll();
        pollDao.createPoll(poll);

        Vote vote = new Vote();
        vote.setVote(true);
        voteDao.createVote(vote, poll);

        voteDao.deleteVote(vote.getId());

        Vote retrievedVote = voteDao.getVoteById(vote.getId());
        assertNull(retrievedVote);
    }

    @Test 
    public void testGetYesVotesByPollId() {
        Poll poll = new Poll();
        pollDao.createPoll(poll);
        
        Vote vote1 = new Vote();
        vote1.setVote(true);
        voteDao.createVote(vote1, poll);

        Vote vote2 = new Vote();
        vote2.setVote(false);
        voteDao.createVote(vote2, poll);

        Vote vote3 = new Vote();
        vote3.setVote(false);
        voteDao.createVote(vote3, poll);

        assertEquals(1, voteDao.getYesVotesByPollId(poll.getId()));
    }

    @Test 
    public void testGetNoVotesByPollId() {
        Poll poll = new Poll();
        pollDao.createPoll(poll);
        
        Vote vote1 = new Vote();
        vote1.setVote(true);
        vote1.setPoll(poll);
        voteDao.createVote(vote1, poll);

        Vote vote2 = new Vote();
        vote2.setVote(false);
        voteDao.createVote(vote2, poll);

        Vote vote3 = new Vote();
        vote3.setVote(false);
        voteDao.createVote(vote3, poll);

        assertEquals(2, voteDao.getNoVotesByPollId(vote1.getPoll().getId()));
    }
}
