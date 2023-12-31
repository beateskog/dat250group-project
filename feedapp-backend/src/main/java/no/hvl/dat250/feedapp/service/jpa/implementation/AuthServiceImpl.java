package no.hvl.dat250.feedapp.service.jpa.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager; 
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import no.hvl.dat250.feedapp.dto.authentication.AuthRequestDTO;
import no.hvl.dat250.feedapp.dto.authentication.AuthResponseDTO;
import no.hvl.dat250.feedapp.dto.authentication.RegisterRequestDTO;
import no.hvl.dat250.feedapp.exception.AccessDeniedException;
import no.hvl.dat250.feedapp.exception.BadRequestException;
import no.hvl.dat250.feedapp.exception.ResourceNotFoundException;
import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.model.Role;
import no.hvl.dat250.feedapp.repository.AccountRepository;
import no.hvl.dat250.feedapp.service.AuthService;
import no.hvl.dat250.feedapp.service.JwtService;

@Service
public class AuthServiceImpl implements AuthService {
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	JwtService jwtService;
	
	@Autowired
	AuthenticationManager authenticationManager;

    @Autowired
    AccountRepository accountRepository;

    @Override
    public AuthResponseDTO register(RegisterRequestDTO registerRequest) {
        Optional<Account> existingAccount = accountRepository.findAccountByUsername(registerRequest.getUsername());
        
        if (existingAccount.isPresent()) {
            throw new BadRequestException("An account with username: " + registerRequest.getUsername() + " already exists.");
        }
       
        if (registerRequest.getUsername() == null 
            || registerRequest.getPassword() == null 
            || registerRequest.getPassword().isBlank() 
            || registerRequest.getUsername().isBlank()) {

            throw new BadRequestException("Both username and password are required fields.");
        }
        
        Account user = new Account();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.USER);
        accountRepository.save(user);
        String jwt = jwtService.generateToken(user);
        return new AuthResponseDTO(jwt);
    }

    @Override
    public AuthResponseDTO authenticate(AuthRequestDTO authRequest) {
        Account user = accountRepository.getByUsername(authRequest.getUsername())
            .orElseThrow(() -> new ResourceNotFoundException("An account with the given username: " + authRequest.getUsername() + " does not exist."));
    
        try {
            String principal = authRequest.getUsername();
            String credentials = authRequest.getPassword();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(principal, credentials));
            String jwt = jwtService.generateToken(user);
            return new AuthResponseDTO(jwt);

        } catch (AuthenticationException e) {
            throw new AccessDeniedException("Wrong password or username.");
        }
        
    }
    
}
