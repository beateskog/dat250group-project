package no.hvl.dat250.feedapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.feedapp.dto.AccountDTO;
import no.hvl.dat250.feedapp.dto.UpdateAccountDTO;
import no.hvl.dat250.feedapp.exception.AccessDeniedException;
import no.hvl.dat250.feedapp.exception.BadRequestException;
import no.hvl.dat250.feedapp.exception.ResourceNotFoundException;
import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.service.AccountService;

/**
 * The controller layer for the account entity
 */
@RestController
@RequestMapping("/account")
@CrossOrigin(origins = "http://localhost:4200")
public class AccountController {

    @Autowired
    private AccountService accountService;

    // READ
    /**
     * Finds an account by id
     * @param accountId the id of the account to find
     * @return the accountDTO of the found account
     * @throws ResourceNotFoundException if the account is not found
     * @throws Exception if something else goes wrong
     * @throws BadRequestException if the id is null
     */
    @GetMapping("/id/{accountId}")
    public ResponseEntity<?> findAccountById(@PathVariable("accountId") Long accountId) {
        try {
            Account account = accountService.findAccountById(accountId);
            return ResponseEntity.ok(AccountDTO.AccountToAccountDTO(account));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch(BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
    /**
     * Finds an account by username
     * @param username the username of the account to find
     * @return the accountDTO of the found account
     * @throws ResourceNotFoundException if the account is not found
     * @throws BadRequestException if the username is null
     * @throws Exception if something else goes wrong
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<?> findAccountByUsername(@PathVariable String username) {
        try {
            Account account = accountService.findAccountByUsername(username);
            return ResponseEntity.ok(AccountDTO.AccountToAccountDTO(account));
        }
        catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch(BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    /**
     * Finds all accounts
     * @return a list of all accounts
     * @throws ResourceNotFoundException if no accounts are found
     * @throws Exception if something else goes wrong
     */
    @GetMapping()
    public ResponseEntity<?> getAllAccounts() {
        try {
            List<Account> accounts = accountService.getAllAccounts();
            List<AccountDTO> accountDTOs = accounts.stream().map(account -> AccountDTO.AccountToAccountDTO(account)).toList();
            return ResponseEntity.ok(accountDTOs);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        
    }

    // UPDATE
    /**
     * Updates an account
     * @param token the token of the user
     * @param account the accountDTO of the account to update
     * @param accountId the id of the account to update
     * @return the updated accountDTO
     * @throws ResourceNotFoundException if the account is not found
     * @throws BadRequestException if the username already exists
     * @throws AccessDeniedException if the user is not updating their own account
     * @throws Exception if something else goes wrong
     * 
     */
    @PutMapping("{accountId}")
    public ResponseEntity<?> updateAccount(UsernamePasswordAuthenticationToken token, @RequestBody UpdateAccountDTO account, @PathVariable("accountId") Long accountId) {
        try {
            Account user = (Account) token.getPrincipal();
            
            Account updatedAccount = accountService.updateAccount(account, user);
            return ResponseEntity.ok(AccountDTO.AccountToAccountDTO(updatedAccount));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // DELETE
    /**
     * Deletes an account by id
     * @param token the token of the user
     * @param accountId the id of the account to delete
     * @return a string confirming that the account was deleted
     * @throws ResourceNotFoundException if the account is not found
     * @throws BadRequestException if the id is null
     * @throws AccessDeniedException if the user is not an admin
     * @throws Exception if something else goes wrong
     */
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
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    /**
     * Deletes the account of the user
     * @param token the token of the user
     * @return a string confirming that the account was deleted
     * @throws ResourceNotFoundException if the account is not found
     * @throws Exception if something else goes wrong
     */
    @DeleteMapping("/username")
    public ResponseEntity<?> deleteMyAccount(UsernamePasswordAuthenticationToken token) {
        try {
            Account user = (Account) token.getPrincipal();
            String resp = accountService.deleteMyAccount(user.getUsername());
            return ResponseEntity.ok().body(resp);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

}