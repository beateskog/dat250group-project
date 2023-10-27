package no.hvl.dat250.feedapp.service.jpa.implementation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import no.hvl.dat250.feedapp.service.implementation.JwtServiceImpl;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations="classpath:application-test.properties")
public class JwtServiceImplTest {

    @Value("${app.secret_key}")
    private String SECRET_KEY;

    @Mock
    UserDetails userDetails;

    @InjectMocks
    JwtServiceImpl jwtService;
    
    @BeforeEach
    public void setup() {
        jwtService.setSecretKey(SECRET_KEY);
    }


    @Test
    public void testGenerateToken() {
        when(userDetails.getUsername()).thenReturn("testUser");

        String jwt = jwtService.generateToken(userDetails);

        assertNotNull(jwt);
        assertEquals("testUser", jwtService.retrieveUsername(jwt));
    }

    @Test
    public void testIsTokenExpired() {
        when(userDetails.getUsername()).thenReturn("testUser");

        String jwt = jwtService.generateToken(userDetails);

        assertFalse(jwtService.isTokenExpired(jwt));
    }

    @Test
    public void testIsTokenValid() {
        when(userDetails.getUsername()).thenReturn("testUser");

        String jwt = jwtService.generateToken(userDetails);

        assertTrue(jwtService.isTokenValid(jwt, userDetails));
    }

   
}
