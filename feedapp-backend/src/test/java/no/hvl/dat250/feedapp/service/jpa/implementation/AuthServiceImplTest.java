package no.hvl.dat250.feedapp.service.jpa.implementation;

import no.hvl.dat250.feedapp.dto.authentication.AuthRequestDTO;
import no.hvl.dat250.feedapp.dto.authentication.RegisterRequestDTO;
import no.hvl.dat250.feedapp.exception.AccessDeniedException;
import no.hvl.dat250.feedapp.exception.BadRequestException;
import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.repository.AccountRepository;
import no.hvl.dat250.feedapp.service.JwtService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void testRegisterUserAlreadyExists() {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setUsername("testUser");
        
        when(accountRepository.findAccountByUsername(anyString())).thenReturn(Optional.of(new Account()));

        assertThrows(BadRequestException.class, () -> authService.register(request));
    }

    @Test
    void testRegisterUsernameOrPasswordNull() {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setUsername(null);
        request.setPassword("testPassword");

        when(accountRepository.findAccountByUsername(null)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> authService.register(request));
    }

    @Test
    void testSuccessfulRegister() {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setUsername("testUser");
        request.setPassword("testPassword");

        when(accountRepository.findAccountByUsername(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(Account.class))).thenReturn("token");

        assertNotNull(authService.register(request));
    }

    @Test
    void testSuccessfulAuthentication() {
        AuthRequestDTO request = new AuthRequestDTO();
        request.setUsername("testUser");
        request.setPassword("testPassword");

        Account mockAccount = new Account();
        mockAccount.setUsername("testUser");
        mockAccount.setPassword("testPassword");

        when(accountRepository.getByUsername(anyString())).thenReturn(mockAccount);
        when(jwtService.generateToken(any(Account.class))).thenReturn("token");

        assertNotNull(authService.authenticate(request));
    }

    @Test
    void testRegisterWithEmptyUsername() {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setUsername("");
        request.setPassword("testPassword");

        assertThrows(BadRequestException.class, () -> authService.register(request));
    }

    @Test
    void testAuthenticationWithNonExistentUser() {
        AuthRequestDTO request = new AuthRequestDTO();
        request.setUsername("nonExistentUser");
        request.setPassword("testPassword");

        when(accountRepository.getByUsername("nonExistentUser")).thenReturn(null);
       
        assertThrows(AccessDeniedException.class, () -> authService.authenticate(request));  
    }
    
    @Test
    void testAuthenticationWithIncorrectPassword() {
        AuthRequestDTO request = new AuthRequestDTO();
        request.setUsername("testUser");
        request.setPassword("wrongPassword");

        Account mockAccount = new Account();
        mockAccount.setUsername("testUser");
        mockAccount.setPassword("correctPassword");

        when(accountRepository.getByUsername("testUser")).thenReturn(mockAccount);
        
        doThrow(new BadCredentialsException("Wrong password"))
        .when(authenticationManager)
        .authenticate(new UsernamePasswordAuthenticationToken("testUser", "wrongPassword"));

        assertThrows(AccessDeniedException.class, () -> authService.authenticate(request));  
    }


}
