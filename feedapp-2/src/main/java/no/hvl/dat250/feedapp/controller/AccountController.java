package no.hvl.dat250.feedapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.feedapp.DTO.AccountDTO;
import no.hvl.dat250.feedapp.exception.AccessDeniedException;
import no.hvl.dat250.feedapp.exception.BadRequestException;
import no.hvl.dat250.feedapp.exception.ResourceNotFoundException;
import no.hvl.dat250.feedapp.exception.UnauthorizedAccessException;
import no.hvl.dat250.feedapp.model.jpa.Account;
import no.hvl.dat250.feedapp.model.jpa.Poll;
import no.hvl.dat250.feedapp.service.jpa.AccountService;


@RestController
@RequestMapping("/account")
public class AccountController {

    // The controller layer communicates with the service layer
    @Autowired
    private AccountService accountService;

    // USE Autowired instead of constructor injection
    //public AccountController(AccountService accountService) {
    //    this.accountService = accountService;
    //}

    // CREATE
    //DONT NEED THIS METHOD SINCE WE NOW HAVE A REGISTER METHOD IN AUTHCONTROLLER
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
    public ResponseEntity<?> updateAccount(UsernamePasswordAuthenticationToken token, @RequestBody Account account, @PathVariable("accountId") Long accountId) {
        try {
            Account user = (Account) token.getPrincipal();
            //Checks if the user that is logged in is the same as the user that is being updated
            if (!user.getId().equals(accountId)) {
                throw new AccessDeniedException("You are not authorized to update this account.");
            }
            Account updatedAccount = accountService.updateAccount(account, accountId);
            return ResponseEntity.ok(AccountToAccountDTO(updatedAccount));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    // DELETE
    @DeleteMapping("/{accountId}")
    public ResponseEntity<?> deleteAccountById(UsernamePasswordAuthenticationToken token, @PathVariable("accountId") Long accountId) {
        try {
            Account user = (Account) token.getPrincipal();
            //Account account = accountService.findAccountById(accountId);
            //We check inside deleteAccountById if the user is an admin
            String resp = accountService.deleteAccountById(user, accountId);
            return ResponseEntity.ok().body(resp);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (UnauthorizedAccessException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        }
    }

    @DeleteMapping("/username")
    public ResponseEntity<?> deleteMyAccount(UsernamePasswordAuthenticationToken token) {
        try {
            Account user = (Account) token.getPrincipal();
            String resp = accountService.deleteMyAccount(user.getUsername());
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