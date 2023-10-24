package no.hvl.dat250.feedapp.security;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import no.hvl.dat250.feedapp.DTO.authentication.AuthRequestDTO;
import no.hvl.dat250.feedapp.exception.BadRequestException;
import no.hvl.dat250.feedapp.service.jpa.AuthService;

import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;
   
    @Test
    public void testRegisterEndpoint() throws Exception {
        String jsonPayload = "{ \"username\": \"testUser\", \"password\": \"testPass\" }"; 

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isOk());
    }

     @Test
    public void testLogin() throws Exception {
        String validPayload = "{\"username\":\"validUser\",\"password\":\"validUserPassword\"}";

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validPayload))
                .andExpect(status().isOk());
    }

    @Test
    public void testFailedLogin_WithInvalidUsername_And_Password() throws Exception {
        String invalidPayload = "{\"username\":\"invalidUser\",\"password\":\"invalidUserPassword\"}";

        when(authService.authenticate(any(AuthRequestDTO.class)))
            .thenThrow(new BadRequestException("Invalid credentials."));

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid credentials."));
    }

    @Test
    public void testFailedLogin_WithValidUsername_And_InvalidPassword() throws Exception {
        String invalidPayload = "{\"username\":\"validUser\",\"password\":\"invalidUserPassword\"}";

        when(authService.authenticate(any(AuthRequestDTO.class)))
            .thenThrow(new BadRequestException("Invalid credentials."));

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid credentials."));
    }
    
}
