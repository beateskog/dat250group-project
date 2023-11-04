package no.hvl.dat250.feedapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import no.hvl.dat250.feedapp.model.Account;


public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Find an account by its username
     * @param username the username of the account
     * @return An optional that contains the account if it exists
     */
    public Optional<Account> getByUsername(String username);

    /**
     * Find an account by its username
     * @param username the username of the account
     * @return An optional that contains the account if it exists
     */
    public Optional<Account> findAccountByUsername(String username);
    
    /**
     * Delete an account by its username
     * @param username the username of the account
     * @return An optional that contains the account if it exists
     */
    public Optional<Account> deleteAccountByUsername(String username);
}