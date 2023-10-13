package no.hvl.dat250.feedapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.feedapp.exception.BadRequestException;
import no.hvl.dat250.feedapp.exception.ResourceNotFoundException;
import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.service.AccountService;

@RestController
@RequestMapping("/account")
public class AccountController {

    // The controller layer communicates with the service layer
    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody Account account) {
        try {
            accountService.createAccount(account);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (BadRequestException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // READ
    @GetMapping("{accountId}")
    public Account findAccountById(@PathVariable("accountId") Long accountId) {
        return accountService.findAccountById(accountId);
    }

    //@GetMapping("/{username}")
    @GetMapping("{username}")
    public ResponseEntity<Account> findAccountByUsername(@PathVariable String username) {
        Optional<Account> account = accountService.findAccountByUsername(username);
    
        if (account.isPresent()) {
            return ResponseEntity.ok(account.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping()
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    // UPDATE
    @PutMapping
    public ResponseEntity<?> updateAccount(@RequestBody Account account) {
        try {
            Account updatedAccount = accountService.updateAccount(account);
            return ResponseEntity.ok(updatedAccount);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE
    @DeleteMapping("{accountId}")
    public ResponseEntity<Account> deleteAccountById(Long accountId) {
        try {
            //Account account = accountService.findAccountById(accountId);
            accountService.deleteAccountById(accountId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Account> deleteMyAccount(String username) {
        try {
            //Account account = accountService.findByUsername(username);
            accountService.deleteMyAccount(username);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}