package no.hvl.dat250.feedapp.service;

import java.util.List;

import no.hvl.dat250.feedapp.model.jpa.Account;


public interface AccountService {

    // CREATE
    public String createAccount(Account account);

    // READ
    public Account findAccountById(Long accountId);
    public Account findAccountByUsername(String username);
    public List<Account> getAllAccounts();

    // UPDATE
    public Account updateAccount(Account account, Long accountId);

    // DELETE
    public String deleteAccountById(Account account, Long accountId);
    public String deleteMyAccount(String username);

}