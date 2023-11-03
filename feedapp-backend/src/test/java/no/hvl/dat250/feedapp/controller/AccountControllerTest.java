package no.hvl.dat250.feedapp.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.hvl.dat250.feedapp.dto.UpdateAccountDTO;
import no.hvl.dat250.feedapp.exception.BadRequestException;
import no.hvl.dat250.feedapp.exception.AccessDeniedException;
import no.hvl.dat250.feedapp.exception.ResourceNotFoundException;
import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.model.Role;
import no.hvl.dat250.feedapp.repository.AccountRepository;
import no.hvl.dat250.feedapp.service.AccountService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AccountControllerTest {

    @MockBean
    private AccountService accountService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountRepository accountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Account mockAccount;
    @BeforeEach
    public void setup() {
        mockAccount = new Account();
        mockAccount.setId(1L);
        mockAccount.setUsername("testUser");
        mockAccount.setPassword("testPassword");
        mockAccount.setRole(Role.USER);
       
    }
    @AfterEach
    public void cleanup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testFindAccountById() throws Exception {
        Long accountId = 1L;

        when(accountService.findAccountById(accountId)).thenReturn(mockAccount);

        mockMvc.perform(get("/account/id/" + accountId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindAccountByUsername() throws Exception {
        String username = "testUser";

        when(accountService.findAccountByUsername(username)).thenReturn(mockAccount);


        mockMvc.perform(get("/account/username/" + username)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllAccounts() throws Exception {
        mockMvc.perform(get("/account")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

   @Test
    public void testUpdateAccountSuccess() throws Exception {
        Long accountId = 1L;
       
        UpdateAccountDTO updateAccountDTO = new UpdateAccountDTO();
        updateAccountDTO.setUsername("updatedUser");
        updateAccountDTO.setPassword("updatedPassword");

        when(accountService.updateAccount(any(UpdateAccountDTO.class), eq(mockAccount))).thenReturn(mockAccount);

        UsernamePasswordAuthenticationToken token = mock(UsernamePasswordAuthenticationToken.class);
        when(token.getPrincipal()).thenReturn(mockAccount);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(token);

        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(put("/account/" + accountId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateAccountDTO)))
                .andExpect(status().isOk());

    }

    @Test
    public void testUpdateAccountUsernameExists() throws Exception {
        Long accountId = 2L;
        UpdateAccountDTO updateAccountDTO = new UpdateAccountDTO();
        updateAccountDTO.setUsername("User");
        updateAccountDTO.setPassword("updatedPassword");

        Account existingAccount = new Account();
        existingAccount.setId(3L);
        existingAccount.setUsername("User");
        existingAccount.setPassword("existingPassword");

        when(accountRepository.findAccountByUsername("User")).thenReturn(Optional.of(existingAccount));

        doAnswer(invocation -> {
        UpdateAccountDTO input = invocation.getArgument(0);
        Account accountToUpdate = invocation.getArgument(1);

        if (accountRepository.findAccountByUsername(input.getUsername()).isPresent()) {
            throw new BadRequestException("An account with username: " + input.getUsername() + " already exists.");
        }
        return accountToUpdate;
        }).when(accountService).updateAccount(any(UpdateAccountDTO.class), eq(mockAccount));

        UsernamePasswordAuthenticationToken token = mock(UsernamePasswordAuthenticationToken.class);
        when(token.getPrincipal()).thenReturn(mockAccount);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(token);

        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(put("/account/" + accountId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateAccountDTO)))
                .andExpect(status().isBadRequest());

    }

   
    @Test
    public void testDeleteAccountByIdSuccess() throws Exception {
        Long accountId = 1L;
        mockAccount.setRole(Role.ADMIN);

        Account accountToDelete   = new Account();
        accountToDelete.setId(2L);
        accountToDelete.setUsername("testUser");
        accountToDelete.setPassword("testPassword");
        accountToDelete.setRole(Role.USER);

        when(accountService.findAccountById(2L)).thenReturn(accountToDelete);

        UsernamePasswordAuthenticationToken token = mock(UsernamePasswordAuthenticationToken.class);
        when(token.getPrincipal()).thenReturn(mockAccount);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(token);

        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(delete("/account/" + accountId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void testDeleteAccountByIdNotFound() throws Exception {
        Long accountId = 20L; 
     
        mockAccount.setRole(Role.ADMIN);

        doAnswer(invocation -> {
            Long inputAccountId = invocation.getArgument(1);
            if (accountId.equals(inputAccountId)) {
                throw new ResourceNotFoundException("Account with ID: " + inputAccountId + " does not exist.");
            }
            return null;
        }).when(accountService).deleteAccountById(any(Account.class), eq(accountId));

        UsernamePasswordAuthenticationToken token = mock(UsernamePasswordAuthenticationToken.class);
        when(token.getPrincipal()).thenReturn(mockAccount);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(token);

        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(delete("/account/" + accountId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) // Expecting 404 Not Found
                .andExpect(content().string("Account with ID: " + accountId + " does not exist."));
    }

   
    @Test
    public void testDeleteAccountByIdNotAdmin() throws Exception {
        Long accountId = 1L;

        doAnswer(invocation -> {
            Account inputAccount = invocation.getArgument(0);
            Long inputAccountId = invocation.getArgument(1);

            if (inputAccount.getRole() != Role.ADMIN) {
                throw new AccessDeniedException("Only administrators can delete accounts.");
            }
            return "Account with ID: " + inputAccountId + " has been successfully deleted";
        }).when(accountService).deleteAccountById(any(Account.class), eq(accountId));

        UsernamePasswordAuthenticationToken token = mock(UsernamePasswordAuthenticationToken.class);
        when(token.getPrincipal()).thenReturn(mockAccount);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(token);

        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(delete("/account/" + accountId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden()) 
                .andExpect(content().string("Only administrators can delete accounts."));
    }


}

