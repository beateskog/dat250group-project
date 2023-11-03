package no.hvl.dat250.feedapp.service.jpa.implementation;

import no.hvl.dat250.feedapp.exception.BadRequestException;
import no.hvl.dat250.feedapp.exception.ResourceNotFoundException;
import no.hvl.dat250.feedapp.dto.UpdateAccountDTO;
import no.hvl.dat250.feedapp.exception.AccessDeniedException;
import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.model.Role;
import no.hvl.dat250.feedapp.repository.AccountRepository;
import no.hvl.dat250.feedapp.service.jpa.implementation.AccountServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAccount_Success() {
        Account account = new Account();
        account.setUsername("testUser");
        account.setPassword("testPassword");

        when(accountRepository.findAccountByUsername("testUser")).thenReturn(Optional.empty());
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        String response = accountService.createAccount(account);

        assertThat(response).isEqualTo("Account Created Successfully");
    }

    @Test
    void createAccount_UsernameExists() {
        Account account = new Account();
        account.setUsername("testUser");
        account.setPassword("testPassword");

        when(accountRepository.findAccountByUsername("testUser")).thenReturn(Optional.of(account));

        assertThatExceptionOfType(BadRequestException.class)
            .isThrownBy(() -> accountService.createAccount(account))
            .withMessage("An account with username: testUser already exists.");
    }

    @Test
    void findAccountById_Success() {
        Long accountId = 1L;
        Account account = new Account();
        account.setId(accountId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        Account response = accountService.findAccountById(accountId);

        assertThat(response).isEqualTo(account);
    }

    @Test
    void findAccountById_NotFound() {
        Long accountId = 1L;

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
            .isThrownBy(() -> accountService.findAccountById(accountId))
            .withMessage("An account with the given ID: 1 does not exist.");
    }

    @Test
    void updateAccount_Success() {
        Long accountId = 1L;
        Account account = new Account();
        account.setId(accountId);
        account.setUsername("testUser");
        account.setPassword("testPassword");

        UpdateAccountDTO updateReq = new UpdateAccountDTO();
        updateReq.setUsername("updatedUser");
        updateReq.setPassword("updatedPassword");

        Account updatedAccount = new Account();
        updatedAccount.setId(accountId);
        updatedAccount.setUsername("updatedUser");
        updatedAccount.setPassword("updatedPassword");

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(updatedAccount);

        Account response = accountService.updateAccount(updateReq, account);

        assertThat(response).isEqualTo(updatedAccount);
    }


    @Test
    void updateAccount_UsernameExists() {
        Long accountId = 1L;
        Account account = new Account();
        account.setId(accountId);
        account.setUsername("testUser");
        account.setPassword("testPassword");

        UpdateAccountDTO updateReq = new UpdateAccountDTO();
        updateReq.setUsername("testUser");
        updateReq.setPassword("testPassword");

        Account existingAccount = new Account();
        existingAccount.setId(accountId);
        existingAccount.setUsername("testUser");
        existingAccount.setPassword("existingPassword");

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountRepository.findAccountByUsername("testUser")).thenReturn(Optional.of(existingAccount));

        assertThatExceptionOfType(BadRequestException.class)
            .isThrownBy(() -> accountService.updateAccount(updateReq, account))
            .withMessage("An account with username: testUser already exists.");
    }

    @Test
    void deleteAccountById_Success() {
        Long accountId = 1L;
        Account account = new Account();
        account.setId(accountId);
        account.setUsername("testUser");
        account.setPassword("testPassword");
        account.setRole(Role.ADMIN);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        String response = accountService.deleteAccountById(account, accountId);

        assertThat(response).isEqualTo("Account with ID: 1 has been successfully deleted");
    }

    @Test
    void deleteAccountById_NotFound() {
        Long accountId = 1L;
        Account admin = new Account();
        admin.setId(accountId);
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setRole(Role.ADMIN);

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
            .isThrownBy(() -> accountService.deleteAccountById(admin, accountId))
            .withMessage("Account with ID: 1 does not exist.");
    }

    @Test
    void deleteAccountById_NotAdmin() {
        Long accountId = 1L;
        Account account = new Account();
        account.setId(accountId);
        account.setUsername("testUser");
        account.setPassword("testPassword");
        account.setRole(Role.USER);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        assertThatExceptionOfType(AccessDeniedException.class)
            .isThrownBy(() -> accountService.deleteAccountById(account, accountId))
            .withMessage("Only administrators can delete accounts.");
    }

    @Test
    void deleteMyAccount_Success() {
        Long accountId = 1L;
        Account account = new Account();
        account.setId(accountId);
        account.setUsername("testUser");
        account.setPassword("testPassword");

        when(accountRepository.findAccountByUsername("testUser")).thenReturn(Optional.of(account));

        String response = accountService.deleteMyAccount("testUser");

        assertThat(response).isEqualTo("Account with username: testUser has been successfully deleted");
    }

    @Test
    void deleteMyAccount_NotFound() {
        String username = "testUser";

        when(accountRepository.findAccountByUsername(username)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
            .isThrownBy(() -> accountService.deleteMyAccount(username))
            .withMessage("Account with username: testUser does not exist.");
    }

}

