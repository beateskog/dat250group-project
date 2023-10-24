package no.hvl.dat250.feedapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import no.hvl.dat250.feedapp.model.jpa.Account;


public interface AccountRepository extends JpaRepository<Account, Long> {

    public Account getByUsername(String username);

    public Optional<Account> findAccountByUsername(String username);
    
    public Optional<Account> deleteAccountByUsername(String username);
}