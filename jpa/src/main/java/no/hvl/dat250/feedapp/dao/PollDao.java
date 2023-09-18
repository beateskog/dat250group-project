package no.hvl.dat250.feedapp.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;
import no.hvl.dat250.feedapp.Poll;

public class PollDao {

    static final String PERSISTENCE_UNIT_NAME = "feedapp";
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    private EntityManager em;

    public PollDao(EntityManager em) {
        this.em = em;
    }

    public void createPoll(Poll poll) {
        em.getTransaction().begin();
        em.persist(poll);
        em.getTransaction().commit();
    }

    public Poll getPollById(Long pollId) {
        Poll poll = em.find(Poll.class, pollId);
        return poll;
    }

    public List<Poll> getAllPolls() {
        List<Poll> polls = em.createQuery("SELECT p FROM Poll p", Poll.class).getResultList();
        return polls;
    }

    public void updatePoll(Poll poll) {
        em.getTransaction().begin();
        em.merge(poll);
        em.getTransaction().commit();
    }

    public void deletePoll(Long pollId) {
        em.getTransaction().begin();
        Poll poll = em.find(Poll.class, pollId);
        if (poll != null) {
            em.remove(poll);
        }
        em.getTransaction().commit();
    }
}
