package no.hvl.dat250.feedapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import no.hvl.dat250.feedapp.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{
    
    Optional<Account> findByUsername(String username);

    @Query("SELECT DISTINCT a FROM Account a LEFT JOIN FETCH a.polls WHERE a.id = :accountId")
    Optional<Account> findAccountWithPolls(@Param("accountId") Long accountId);  
    
    @Query("SELECT DISTINCT a FROM Account a LEFT JOIN FETCH a.polls WHERE a.username = :username")
    Optional<Account> findAccountByUsernameWithPolls(@Param("username") String username);
}
