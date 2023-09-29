package no.hvl.dat250.feedapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import no.hvl.dat250.feedapp.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{
    
    Optional<Account> findByUsername(String username);
}
