package no.hvl.dat250.feedapp.controller;

import java.util.List;

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

import no.hvl.dat250.feedapp.DTO.AccountDTO;
import no.hvl.dat250.feedapp.exception.BadRequestException;
import no.hvl.dat250.feedapp.exception.ResourceNotFoundException;
import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.model.Poll;
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
            return ResponseEntity.status(HttpStatus.CREATED).body(AccountToAccountDTO(account));
        } catch (BadRequestException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // READ
    @GetMapping("/id/{accountId}")
    public ResponseEntity<?> findAccountById(@PathVariable("accountId") Long accountId) {
        try {
            Account account = accountService.findAccountById(accountId);
            return ResponseEntity.ok(AccountToAccountDTO(account));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> findAccountByUsername(@PathVariable String username) {
        try {
            Account account = accountService.findAccountByUsername(username);
            return ResponseEntity.ok(AccountToAccountDTO(account));
        }
        catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<?> getAllAccounts() {
        try {
            List<Account> accounts = accountService.getAllAccounts();
            List<AccountDTO> accountDTOs = accounts.stream().map(account -> AccountToAccountDTO(account)).toList();
            return ResponseEntity.ok(accountDTOs);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());

        }
        
    }

    // UPDATE
    @PutMapping("{accountId}")
    public ResponseEntity<?> updateAccount(@RequestBody Account account, @PathVariable("accountId") Long accountId) {
        try {
            Account updatedAccount = accountService.updateAccount(account, accountId);
            return ResponseEntity.ok(AccountToAccountDTO(updatedAccount));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    // DELETE
    @DeleteMapping("/{accountId}")
    public ResponseEntity<?> deleteAccountById(@PathVariable("accountId") Long accountId) {
        try {
            //Account account = accountService.findAccountById(accountId);
            String resp = accountService.deleteAccountById(accountId);
            return ResponseEntity.ok().body(resp);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @DeleteMapping("/username/{username}")
    public ResponseEntity<?> deleteMyAccount(@PathVariable("username") String username) {
        try {
            String resp = accountService.deleteMyAccount(username);
            return ResponseEntity.ok().body(resp);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    public AccountDTO AccountToAccountDTO (Account account) {

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.id = account.getId();
        accountDTO.username = account.getUsername();
        accountDTO.role = account.getRole().toString();
        accountDTO.numberOfpolls = account.getPolls().size();
        accountDTO.polls = new java.util.ArrayList<Long>();
        for (Poll poll : account.getPolls()) {
            accountDTO.polls.add(poll.getId());
        }

        return accountDTO;
    }
}