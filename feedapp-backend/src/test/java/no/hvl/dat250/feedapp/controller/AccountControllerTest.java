package no.hvl.dat250.feedapp.controller;

import org.junit.jupiter.api.AfterEach;
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
import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.model.Role;
import no.hvl.dat250.feedapp.service.AccountService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    public void cleanup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testFindAccountById() throws Exception {
        Long accountId = 1L;
        Account mockAccount = new Account();
        mockAccount.setId(accountId);
        mockAccount.setUsername("testUser");
        mockAccount.setRole(Role.USER);
        mockAccount.setPassword("testPassword");

        when(accountService.findAccountById(accountId)).thenReturn(mockAccount);

        mockMvc.perform(get("/account/id/" + accountId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindAccountByUsername() throws Exception {
        String username = "testUser";
        Account mockAccount = new Account();
        mockAccount.setId(1L);
        mockAccount.setUsername(username);
        mockAccount.setRole(Role.USER);
        mockAccount.setPassword(username);

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
        Account mockAccount = new Account();
        mockAccount.setId(accountId);
        mockAccount.setUsername("testUser");
        mockAccount.setRole(Role.USER);
        mockAccount.setPassword("testPassword");

        UpdateAccountDTO updateAccountDTO = new UpdateAccountDTO();
        updateAccountDTO.setUsername("testUser");
        updateAccountDTO.setPassword("testPassword");

        // Ensure that service is mocked correctly
        when(accountService.updateAccount(any(UpdateAccountDTO.class), eq(mockAccount))).thenReturn(mockAccount);

        // Mock the Authentication
        UsernamePasswordAuthenticationToken authentication = mock(UsernamePasswordAuthenticationToken.class);
        when(authentication.getPrincipal()).thenReturn(mockAccount);

        // Mock SecurityContext
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        
        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(put("/account/" + accountId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateAccountDTO)))
                .andExpect(status().isOk());

    }



}

