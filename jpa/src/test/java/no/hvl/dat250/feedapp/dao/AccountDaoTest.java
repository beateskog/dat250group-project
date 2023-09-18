package no.hvl.dat250.feedapp.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import no.hvl.dat250.feedapp.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


public class AccountDaoTest {
    private static final String PERSISTENCE_UNIT_NAME = "feedapp";
    private EntityManagerFactory emf;
    private EntityManager em;
    private AccountDao accountDao;

    @Before
    public void setUp() {
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = emf.createEntityManager();
        accountDao = new AccountDao(em);
    }

    @After
    public void tearDown() {
        em.close();
        emf.close();
    }

    @Test
    public void testCreateAccount() {
        Account user = new Account();
        user.setUsername("testUser");
        user.setPassword("password");
        accountDao.createAccount(user);

        Account retrievedUser = accountDao.getAccountById(user.getId());
        assertNotNull(retrievedUser);
        assertEquals("testUser", retrievedUser.getUsername());
       
    }

    @Test 
    public void testGetAllAccounts() {
        Account user1 = new Account();
        user1.setUsername("testUser1");
        user1.setPassword("password1");
        accountDao.createAccount(user1);

        Account user2 = new Account();
        user2.setUsername("testUser2");
        user2.setPassword("password2");
        accountDao.createAccount(user2);

        assertEquals(2, accountDao.getAllAccounts().size());
    }

    @Test 
    public void testUpdateAccount() {
        Account user = new Account();
        user.setUsername("testUser");
        user.setPassword("password");
        accountDao.createAccount(user);

        user.setUsername("newUsername");
        accountDao.updateAccount(user);

        Account retrievedUser = accountDao.getAccountById(user.getId());
        assertEquals("newUsername", retrievedUser.getUsername());
    }

    @Test 
    public void testDeleteAccount() {
        Account user = new Account();
        user.setUsername("testUser");
        user.setPassword("password");
        accountDao.createAccount(user);
        Long id = user.getId();

        accountDao.deleteAccount(id);

        Account retrievedUser = accountDao.getAccountById(id);
        assertNull(retrievedUser);
    }
}

