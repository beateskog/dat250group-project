package no.hvl.dat250.feedapp.service;

import java.util.List;

import no.hvl.dat250.feedapp.dto.UpdateAccountDTO;
import no.hvl.dat250.feedapp.exception.AccessDeniedException;
import no.hvl.dat250.feedapp.exception.BadRequestException;
import no.hvl.dat250.feedapp.exception.ResourceNotFoundException;
import no.hvl.dat250.feedapp.model.Account;


public interface AccountService {

    // CREATE
    /**
     * Creates a new account and saves it to the database.
     * @param account The account to be created.
     * @return A string confirming that the account was created successfully.
     * @throws BadRequestException If the username or password is null or blank.
     * @throws BadRequestException If the username already exists.
     */
    public String createAccount(Account account);

    // READ
     /**
     * Finds an account by its ID.
     * @param accountId The ID of the account to be found.
     * @return The account with the given ID.
     * @throws ResourceNotFoundException If an account with the given ID does not exist.
     */
    public Account findAccountById(Long accountId);

    /**
     * Finds an account by its username.
     * @param username The username of the account to be found.
     * @return The account with the given username.
     * @throws ResourceNotFoundException If an account with the given username does not exist.
     */
    public Account findAccountByUsername(String username);

    /**
     * Finds all accounts in the database.
     * @return A list of all accounts in the database.
     * If there are no accounts in the database, an empty list is returned.
     */
    public List<Account> getAllAccounts();

    // UPDATE
    /**
     * Updates an account and saves it to the database.
     * @param account The accountDTO for the account to be updated.
     * @param accountToUpdate The account to be updated.
     * @return The updated account.
     * @throws BadRequestException If the username already exists.
     */
    public Account updateAccount(UpdateAccountDTO account, Account accountToUpdate);

    // DELETE
    /**
     * Deletes an account from the database.
     * @param account The account of the user deleting the account. Must be an administrator.
     * @param accountId The ID of the account to be deleted.
     * @return A string confirming that the account was successfully deleted.
     * @throws AccessDeniedException If the user is not an administrator.
     * @throws ResourceNotFoundException If an account with the given ID does not exist.
     */
    public String deleteAccountById(Account account, Long accountId);

    /**
     * SHOULD ONLY BE USED FOR TESTING PURPOSES, OR UPDATED IF WE WANT TO ALLOW USERS TO DELETE THEIR OWN ACCOUNTS
     * Deletes an account from the database.
     * @param username The username of the account to be deleted.
     * @return A string confirming that the account was successfully deleted.
     */
    public String deleteMyAccount(String username);

}