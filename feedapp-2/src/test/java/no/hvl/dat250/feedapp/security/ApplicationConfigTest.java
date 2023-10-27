package no.hvl.dat250.feedapp.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.Authentication;

import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.repository.AccountRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ApplicationConfigTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private ApplicationConfig applicationConfig;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUserDetailsServiceReturnsCorrectUser() {
        UserDetailsService service = applicationConfig.userDetailsService();

        // Scenario 1: User doesn't exist
        String nonexistentUsername = "nonexistentUser";
        when(accountRepository.getByUsername(nonexistentUsername)).thenReturn(null);
        assertNull(service.loadUserByUsername(nonexistentUsername));

        // Scenario 2: User exists
        String existingUsername = "testUser";
        Account mockAccount = mock(Account.class); 
        when(mockAccount.getUsername()).thenReturn(existingUsername);
        when(accountRepository.getByUsername(existingUsername)).thenReturn(mockAccount);

        UserDetails retrievedUser = service.loadUserByUsername(existingUsername);
        assertNotNull(retrievedUser);
        assertEquals(existingUsername, retrievedUser.getUsername());
    }

    @Test
    public void testAuthenticationProviderSetup() {
        String rawPassword = "testPassword";
        String encodedPassword = applicationConfig.passwordEncoder().encode(rawPassword);
        
        Account mockAccount = mock(Account.class);
        when(mockAccount.getPassword()).thenReturn(encodedPassword);
        when(mockAccount.getUsername()).thenReturn("testUser");
        when(mockAccount.isAccountNonLocked()).thenReturn(true);
        when(mockAccount.isAccountNonExpired()).thenReturn(true);  
        when(mockAccount.isCredentialsNonExpired()).thenReturn(true);  
        when(mockAccount.isEnabled()).thenReturn(true);  
        when(accountRepository.getByUsername("testUser")).thenReturn(mockAccount);
        
       
        DaoAuthenticationProvider provider = (DaoAuthenticationProvider) applicationConfig.authenticationProvider();
        assertNotNull(provider);
        
        Authentication auth = new UsernamePasswordAuthenticationToken("testUser", rawPassword);
        Authentication result = provider.authenticate(auth);
        
        assertNotNull(result);
        assertTrue(result.isAuthenticated());
    }

    @Test
    public void testPasswordEncoder() {
        assertTrue(applicationConfig.passwordEncoder() instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testSecretKeySetterGetter() {
        String testKey = "testKey";
        applicationConfig.setSecretKey(testKey);
        assertEquals(testKey, applicationConfig.getSecretKey());
    }
}
