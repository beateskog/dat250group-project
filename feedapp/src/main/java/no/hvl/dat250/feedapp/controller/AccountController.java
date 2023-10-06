package no.hvl.dat250.feedapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import no.hvl.dat250.feedapp.Account;
import no.hvl.dat250.feedapp.Role;
import no.hvl.dat250.feedapp.DTO.AccountDTO;
import no.hvl.dat250.feedapp.repositories.AccountRepository;

@RestController
@RequestMapping("/account")
public class AccountController {
    
    @Autowired
    private AccountRepository accountRepository;

    @PostMapping("")
    public ResponseEntity<?> createAccount(@RequestBody Account account) {
        try {
            account.setRole(Role.USER);
            accountRepository.save(account);
            return ResponseEntity.ok(AccountToAccountDTO(account));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAccountByUsername(@RequestParam(value = "username", required = true) String username) {
        try {
            Account account = accountRepository.findAccountByUsernameWithPolls(username)
                .orElseThrow(() -> new RuntimeException("Account with username " + username + " not found"));
            return ResponseEntity.ok(AccountToAccountDTO(account));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable(value = "id", required = true) String id) {
        try {
            Account account = accountRepository.findAccountWithPolls(Long.parseLong(id))
                .orElseThrow(() -> new RuntimeException("Account with id " + id + " not found"));
            return ResponseEntity.ok(AccountToAccountDTO(account));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccount(@PathVariable(value = "id", required = true) String id, @RequestBody String username) {
        try {
            Account account = accountRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new RuntimeException("Account with id " + id + " not found"));
            account.setUsername(username);
            accountRepository.save(account);
            return ResponseEntity.ok(AccountToAccountDTO(account));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}") 
    public ResponseEntity<?> deleteAccount(@PathVariable(value = "id", required = true) String id) {
        try {
            Account account = accountRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new RuntimeException("Account with id " + id + " not found"));
            accountRepository.delete(account);
            return ResponseEntity.ok(AccountToAccountDTO(account));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        }
    }

    public AccountDTO AccountToAccountDTO (Account account) {

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.id = account.getId();
        accountDTO.username = account.getUsername();
        accountDTO.role = account.getRole().toString();
        accountDTO.numberOfpolls = account.getPolls().size();
        accountDTO.polls = new java.util.ArrayList<Long>();
        for (no.hvl.dat250.feedapp.Poll poll : account.getPolls()) {
            accountDTO.polls.add(poll.getId());
        }

        return accountDTO;
    }

}
