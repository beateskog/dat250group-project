package no.hvl.dat250.feedapp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import no.hvl.dat250.feedapp.dto.authentication.AccountRespDTO;
import no.hvl.dat250.feedapp.dto.authentication.AuthRequestDTO;
import no.hvl.dat250.feedapp.dto.authentication.AuthResponseDTO;
import no.hvl.dat250.feedapp.dto.authentication.RegisterRequestDTO;
import no.hvl.dat250.feedapp.exception.AccessDeniedException;
import no.hvl.dat250.feedapp.exception.BadRequestException;
import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.model.Role;
import no.hvl.dat250.feedapp.repository.AccountRepository;
import no.hvl.dat250.feedapp.service.AuthService;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerTest {

    @MockBean
    private AuthService authService;

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
    public void testRegisterUser() throws Exception {
        RegisterRequestDTO registerRequest = new RegisterRequestDTO();
        registerRequest.setUsername("testUser");
        registerRequest.setPassword("testPassword");

        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setjwt("testJWT");

        when(authService.register(any(RegisterRequestDTO.class))).thenReturn(authResponseDTO);

        mockMvc.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registerRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(authResponseDTO)));

        verify(authService, times(1)).register(any(RegisterRequestDTO.class));
    }

    @Test
    public void testRegisterUserBadRequest() throws Exception {
        RegisterRequestDTO registerRequest = new RegisterRequestDTO();
        registerRequest.setUsername("testUser");
        registerRequest.setPassword("testPassword");

        when(authService.register(any(RegisterRequestDTO.class))).thenThrow(new BadRequestException("User already exists"));

        mockMvc.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registerRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("User already exists"));

        verify(authService, times(1)).register(any(RegisterRequestDTO.class));
    }

    @Test
    public void testRegisterUserInternalServerError() throws Exception {
        RegisterRequestDTO registerRequest = new RegisterRequestDTO();
        registerRequest.setUsername("testUser");
        registerRequest.setPassword("testPassword");

        when(authService.register(any(RegisterRequestDTO.class))).thenThrow(new RuntimeException("Something went wrong"));

        mockMvc.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registerRequest)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().string("Something went wrong"));

        verify(authService, times(1)).register(any(RegisterRequestDTO.class));
    }

    @Test
    public void testLogin() throws Exception {
        AuthRequestDTO authRequest = new AuthRequestDTO();
        authRequest.setUsername("testUser");
        authRequest.setPassword("testPassword");

        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setjwt("testJWT");

        when(authService.authenticate(any(AuthRequestDTO.class))).thenReturn(authResponseDTO);

        mockMvc.perform(post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(authRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(authResponseDTO)));

        verify(authService, times(1)).authenticate(any(AuthRequestDTO.class));
    }

    @Test
    public void testLoginAccessDenied() throws Exception {
        AuthRequestDTO authRequest = new AuthRequestDTO();
        authRequest.setUsername("testUser");
        authRequest.setPassword("testPassword");

        when(authService.authenticate(any(AuthRequestDTO.class))).thenThrow(new AccessDeniedException("User already exists"));

        mockMvc.perform(post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(authRequest)))
                    .andExpect(status().isForbidden())
                    .andExpect(content().string("User already exists"));

        verify(authService, times(1)).authenticate(any(AuthRequestDTO.class));
    }

    @Test
    public void testgetUser() throws Exception {
        AccountRespDTO accountRespDTO = new AccountRespDTO();
        accountRespDTO.setUsername("testUser");

        UsernamePasswordAuthenticationToken token = mock(UsernamePasswordAuthenticationToken.class);
        when(token.getPrincipal()).thenReturn(mockAccount);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(token);
 
        SecurityContextHolder.setContext(securityContext);
        
        mockMvc.perform(post("/auth/user")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(accountRespDTO)));

    }

    
}
