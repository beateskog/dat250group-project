package no.hvl.dat250.feedapp.dao;

import jakarta.persistence.EntityManager;
import java.util.List;
import no.hvl.dat250.feedapp.Account;

public class AccountDao {

    static final String PERSISTENCE_UNIT_NAME = "feedapp";
    private EntityManager em;

    public AccountDao(EntityManager em) {
        this.em = em;
    }

    public void createAccount(Account account) {
        em.getTransaction().begin();
        em.persist(account);
        em.getTransaction().commit();
    }

    public Account getAccountById(Long accountId) {
        Account account = em.find(Account.class, accountId);
        return account;
    }

    public List<Account> getAllAccounts() {
        List<Account> accounts = em.createQuery("SELECT a FROM Account a", Account.class).getResultList();
        return accounts;
    }

    public void updateAccount(Account account) {
        em.getTransaction().begin();
        em.merge(account);
        em.getTransaction().commit();
    }

    public void deleteAccount(Long accountId) {
        em.getTransaction().begin();
        Account account = em.find(Account.class, accountId);
        if (account != null) {
            em.remove(account);
        }
        em.getTransaction().commit();
    }
}
