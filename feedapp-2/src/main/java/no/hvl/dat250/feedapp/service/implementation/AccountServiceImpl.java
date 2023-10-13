package no.hvl.dat250.feedapp.service.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import no.hvl.dat250.feedapp.exception.BadRequestException;
import no.hvl.dat250.feedapp.exception.ResourceNotFoundException;
import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.model.Role;
import no.hvl.dat250.feedapp.repository.AccountRepository;
import no.hvl.dat250.feedapp.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    public AccountServiceImpl (AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // -------------------------------------------------- CREATE -------------------------------------------------------
    
    // 1. Check if username is unique (there does not already exist a user with the same username)
    // 2. Username and password are required fields in order to create a user
    // 3. HVIS VI HAR TID/SENERE: Check password strength / qualifications
    // 4. The id and Role.USER is assigned automatically upon creation
    @Override
    public String createAccount(Account account) {
        Optional<Account> existingAccount = accountRepository.findAccountByUsername(account.getUsername());
        
        if (existingAccount.isPresent()) {
            throw new BadRequestException("An account with username: " + account.getUsername() + " already exists.");
        }
       
        if (account.getUsername() == null || account.getPassword() == null) {
            throw new BadRequestException("Both username and password are required fields.");
        }
        
        // if (!isPasswordStrongEnough(account.getPassword())) {
        //     throw a BadRequestException("Password is too weak."); // Password must be at least 8-characters long elns
        // }

        account.setRole(Role.USER);
        accountRepository.save(account);
        return "Account Created Successfully";
    }

    // --------------------------------------------------- READ --------------------------------------------------------
    @Override
    public Account findAccountById(Long accountId) {
        if (accountRepository.findById(accountId).isEmpty()) {
            throw new ResourceNotFoundException("Requested account with ID: " + accountId + " does not exist.");
        }
        return accountRepository.findById(accountId).get();
    }

    @Override
    public Optional<Account> findAccountByUsername(String username) {
        Optional<Account> account = accountRepository.findAccountByUsername(username);
        if (account.isEmpty()) {
            throw new ResourceNotFoundException("An account with the given username: " + username + "does not exist.");
        }
        return account;
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    // -------------------------------------------------- UPDATE -------------------------------------------------------

    @Override
    public Account updateAccount(Account account) {
        Optional<Account> existingAccountOptional = accountRepository.findById(account.getId());

        if (existingAccountOptional.isEmpty()) {
            throw new ResourceNotFoundException("Account with ID " + account.getId() + " does not exist.");
        }

        Account existingAccount = existingAccountOptional.get();
        if (account.getUsername() != null) {
            existingAccount.setUsername(account.getUsername());
        }

        if (account.getPassword() != null) {
            existingAccount.setPassword(account.getPassword());
        }

        return accountRepository.save(existingAccount);
    }

    // -------------------------------------------------- DELETE -------------------------------------------------------

    @Override
    public String deleteAccountById(Long accountId) {
        if (accountRepository.findById(accountId).isEmpty()) {
            throw new ResourceNotFoundException("Account with ID: " + accountId + " does not exist.");
        }
        // HVIS VI HAR TID/SENERE: Check if the current user (e.g., authenticated user) is an administrator using Spring security

        accountRepository.deleteById(accountId);
        return "Account with ID: " + accountId + " has been successfully deleted";
    }

    // Vi må vurdere om vi skal ha denne metoden eller ikke. I så tilfelle bør vi sjekke ut Spring Security.
    @Override
    public String deleteMyAccount(String username) {
        return "";
    }
}