package no.hvl.dat250.feedapp.dao;

import jakarta.persistence.EntityManager;
import java.util.List;

import no.hvl.dat250.feedapp.Poll;
import no.hvl.dat250.feedapp.Vote;

public class VoteDao {

    static final String PERSISTENCE_UNIT_NAME = "feedapp";
    private EntityManager em;

    public VoteDao(EntityManager em) {
        this.em = em;
    }

    public void createVote(Vote vote, Poll poll) {
        em.getTransaction().begin();
        vote.setPoll(poll);
        em.persist(vote);
        em.getTransaction().commit();
    }

    public Vote getVoteById(Long voteId) {
        Vote vote = em.find(Vote.class, voteId);
        return vote;
    }

    public List<Vote> getAllVotesbyPollId(Long pollId) {
        List<Vote> votes = em.createQuery("SELECT v FROM Vote v WHERE v.poll.id = :pollId", Vote.class)
                .setParameter("pollId", pollId).getResultList();
        return votes;
    }

    public void updateVote(Vote vote) {
        em.getTransaction().begin();
        em.merge(vote);
        em.getTransaction().commit();
    }

    public void deleteVote(Long voteId) {
        em.getTransaction().begin();
        Vote vote = em.find(Vote.class, voteId);
        if (vote != null) {
            em.remove(vote);
        }
        em.getTransaction().commit();
    }

    public int getYesVotesByPollId(Long pollId) {
        List<Vote> votes = em.createQuery("SELECT v FROM Vote v WHERE v.poll.id = :pollId AND v.vote = true", Vote.class)
                .setParameter("pollId", pollId).getResultList();
        return votes.size();
    }

    public int getNoVotesByPollId(Long pollId) {
        List<Vote> votes = em.createQuery("SELECT v FROM Vote v WHERE v.poll.id = :pollId AND v.vote = false", Vote.class)
                .setParameter("pollId", pollId).getResultList();
        return votes.size();
    }
}
