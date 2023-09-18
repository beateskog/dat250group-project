package no.hvl.dat250.feedapp.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import no.hvl.dat250.feedapp.Poll;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class PollDaoTest {
    private static final String PERSISTENCE_UNIT_NAME = "feedapp"; 
    private EntityManagerFactory emf;
    private EntityManager em;
    private PollDao pollDao;

    @Before
    public void setUp() {
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = emf.createEntityManager();
        pollDao = new PollDao(em);
    }

    @After
    public void tearDown() {
        em.close();
        emf.close();
    }

    @Test
    public void testCreatePoll() {
        Poll poll = new Poll();
        poll.setPollPin(12345);
        poll.setQuestion("Test question");
        pollDao.createPoll(poll);

        Poll retrievedPoll = pollDao.getPollById(poll.getId());
        assertNotNull(retrievedPoll);
        assertEquals(12345, retrievedPoll.getPollPin());
        assertEquals("Test question", retrievedPoll.getQuestion());
    }

    @Test
    public void testGetAllPolls() {
        Poll poll1 = new Poll();
        poll1.setPollPin(12345);
        poll1.setQuestion("Test question 1");
        pollDao.createPoll(poll1);

        Poll poll2 = new Poll();
        poll2.setPollPin(54321);
        poll2.setQuestion("Test question 2");
        pollDao.createPoll(poll2);

        assertEquals(2, pollDao.getAllPolls().size());
    }

    @Test
    public void testUpdatePoll() {
        Poll poll = new Poll();
        poll.setPollPin(12345);
        poll.setQuestion("Test question");
        pollDao.createPoll(poll);

        poll.setQuestion("Updated question");
        pollDao.updatePoll(poll);

        Poll retrievedPoll = pollDao.getPollById(poll.getId());
        assertEquals("Updated question", retrievedPoll.getQuestion());
    }

    @Test
    public void testDeletePoll() {
        Poll poll = new Poll();
        poll.setPollPin(12345);
        poll.setQuestion("Test question");
        pollDao.createPoll(poll);

        pollDao.deletePoll(poll.getId());

        Poll retrievedPoll = pollDao.getPollById(poll.getId());
        assertNull(retrievedPoll);
    }
    
}
