package no.hvl.dat250.feedapp.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import no.hvl.dat250.feedapp.DTO.authentication.AuthRequestDTO;
import no.hvl.dat250.feedapp.DTO.authentication.AuthResponseDTO;
import no.hvl.dat250.feedapp.DTO.authentication.RegisterRequestDTO;
import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.model.Role;
import no.hvl.dat250.feedapp.repository.AccountRepository;
import no.hvl.dat250.feedapp.service.AuthService;
import no.hvl.dat250.feedapp.service.JwtService;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
	AccountRepository repository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	JwtService jwtService;
	
	@Autowired
	AuthenticationManager authenticationManager;

    @Override
    public AuthResponseDTO register(RegisterRequestDTO registerRequest) {
        Account user = new Account();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.USER);
        repository.save(user);
        String jwt = jwtService.generateToken(user);
        return new AuthResponseDTO(jwt);
    }

    @Override
    public AuthResponseDTO authenticate(AuthRequestDTO authRequest) {
        //check if user exists
        Account user = repository.getByUsername(authRequest.getUsername());

        String principal = authRequest.getUsername();
        String credentials = authRequest.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(principal, credentials));

        String jwt = jwtService.generateToken(user);
        return new AuthResponseDTO(jwt);
    }
    
}
