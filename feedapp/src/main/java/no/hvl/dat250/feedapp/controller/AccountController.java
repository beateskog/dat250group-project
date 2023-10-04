package no.hvl.dat250.feedapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.feedapp.Account;
import no.hvl.dat250.feedapp.repositories.AccountRepository;

@RestController
public class AccountController {
    
    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/account")
    public ResponseEntity<?> getAccountByUsername(@RequestParam(value = "username", required = true) String username) {
        try {
            Account account = accountRepository.findAccountByUsernameWithPolls(username)
                .orElseThrow(() -> new RuntimeException("Account with username " + username + " not found"));
            return ResponseEntity.ok(account);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        }
    }


}
