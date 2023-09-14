package no.hvl.dat250.feedapp;

import static org.junit.Assert.assertEquals;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import org.junit.Before;
import org.junit.Test;

public class JpaTest {

    private static final String PERSISTENCE_UNIT_NAME = "feedapp";
    private EntityManagerFactory factory;

    @Before
    public void setUp() {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();

        // Begin a new local transaction so that we can persist a new entity
        em.getTransaction().begin();

        // read the existing entries
        Query q = em.createQuery("select m from Account m");
        // Persons should be empty

        // do we have entries?
        boolean createNewEntries = (q.getResultList().size() == 0);

        // No, so lets create new entries
        if (createNewEntries) {
            assertEquals(0, q.getResultList().size());
            Account account = new Account();
            account.setRole(Role.USER);
            account.setUsername("test123");
            account.setPassword("test123");
            em.persist(account);
            for (int i = 0; i < 40; i++) {
                Poll poll = new Poll();
                poll.setQuestion("test_" + i);
                poll.setPollPin(i);
                poll.setPollUrl("https//test_" + i + ".com");
                poll.setStartTime(java.time.LocalDateTime.now());
                poll.setEndTime(java.time.LocalDateTime.of(2023, 10, 10, 10, 10));
                account.getPolls().add(poll);
                em.persist(poll);
                em.persist(account);
            }
        }

        // Commit the transaction, which will cause the entity to
        // be stored in the database
        em.getTransaction().commit();

        // It is always good practice to close the EntityManager so that
        // resources are conserved.
        em.close();

    }

    @Test
    public void checkAvailablePeople() {

        // now lets check the database and see if the created entries are there
        // create a fresh, new EntityManager
        EntityManager em = factory.createEntityManager();

        // Perform a simple query for all the Message entities
        Query q = em.createQuery("select p from Poll p");

        // We should have 40 polls in the database
        assertEquals(40, q.getResultList().size());

        em.close();
    }

    @Test
    public void checkFamily() {
        EntityManager em = factory.createEntityManager();
        
        Query q = em.createQuery("select a from Account a");

        // We should have one account with 40 polls
        assertEquals(1, q.getResultList().size());
        assertEquals(40, ((Account) q.getSingleResult()).getPolls().size());
        em.close();
    }
}