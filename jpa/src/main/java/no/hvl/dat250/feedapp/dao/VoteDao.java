package no.hvl.dat250.feedapp.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;
import no.hvl.dat250.feedapp.Vote;

public class VoteDao {

    static final String PERSISTENCE_UNIT_NAME = "feedapp";
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    private EntityManager em;

    public VoteDao(EntityManager em) {
        this.em = em;
    }

    public void createVote(Vote vote) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(vote);
        em.getTransaction().commit();
        em.close();
    }

    public Vote getVoteById(Long voteId) {
        EntityManager em = emf.createEntityManager();
        Vote vote = em.find(Vote.class, voteId);
        em.close();
        return vote;
    }

    public List<Vote> getAllVotes() {
        EntityManager em = emf.createEntityManager();
        List<Vote> votes = em.createQuery("SELECT v FROM Vote v", Vote.class).getResultList();
        em.close();
        return votes;
    }

    public void updateVote(Vote vote) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(vote);
        em.getTransaction().commit();
        em.close();
    }

    public void deleteVote(Long voteId) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Vote vote = em.find(Vote.class, voteId);
        if (vote != null) {
            em.remove(vote);
        }
        em.getTransaction().commit();
        em.close();
    }
}
