package no.hvl.dat250.feedapp.service;

import java.util.List;
import java.util.Optional;

import no.hvl.dat250.feedapp.model.Account;

public interface AccountService {

    // CREATE
    public String createAccount(Account account);

    // READ
    public Account findAccountById(Long accountId);
    public Optional<Account> findAccountByUsername(String username);
    public List<Account> getAllAccounts();

    // UPDATE
    public Account updateAccount(Account account);

    // DELETE
    public String deleteAccountById(Long accountId);
    public String deleteMyAccount(String username);

}