package no.hvl.dat250.feedapp.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import no.hvl.dat250.feedapp.Vote;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class VoteDaoTest {
    
    private static final String PERSISTENCE_UNIT_NAME = "feedapp"; 
    private EntityManagerFactory emf;
    private EntityManager em;
    private VoteDao voteDao;

    @Before
    public void setUp() {
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = emf.createEntityManager();
        voteDao = new VoteDao(em);
    }

    @After
    public void tearDown() {
        em.close();
        emf.close();
    }

    @Test
    public void testCreateVote() {
        Vote vote = new Vote();
        vote.setVote(true);
        voteDao.createVote(vote);

        Vote retrievedVote = voteDao.getVoteById(vote.getId());
        assertNotNull(retrievedVote);
        assertEquals(true, retrievedVote.isVote());
    }

    @Test
    public void testGetAllVotes() {
        Vote vote1 = new Vote();
        vote1.setVote(true);
        voteDao.createVote(vote1);

        Vote vote2 = new Vote();
        vote2.setVote(false);
        voteDao.createVote(vote2);

        assertEquals(2, voteDao.getAllVotes().size());
    }

    @Test
    public void testUpdateVote() {
        Vote vote = new Vote();
        vote.setVote(true);
        voteDao.createVote(vote);

        vote.setVote(false);
        voteDao.updateVote(vote);

        Vote retrievedVote = voteDao.getVoteById(vote.getId());
        assertEquals(false, retrievedVote.isVote());
    }

    @Test
    public void testDeleteVote() {
        Vote vote = new Vote();
        vote.setVote(true);
        voteDao.createVote(vote);

        voteDao.deleteVote(vote.getId());

        Vote retrievedVote = voteDao.getVoteById(vote.getId());
        assertNull(retrievedVote);
    }
}
