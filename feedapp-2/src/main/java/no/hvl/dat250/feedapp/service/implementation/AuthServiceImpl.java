package no.hvl.dat250.feedapp.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import no.hvl.dat250.feedapp.DTO.AuthResponse;
import no.hvl.dat250.feedapp.authentication.AuthRequest;
import no.hvl.dat250.feedapp.authentication.RegisterRequest;
import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.repository.AccountRepository;
import no.hvl.dat250.feedapp.service.AuthService;
import no.hvl.dat250.feedapp.service.JwtService;


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
    public AuthResponse register(RegisterRequest registerRequest) {
        Account user = new Account();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        throw new UnsupportedOperationException("Unimplemented method 'register'");
    }

    @Override
    public AuthResponse authenticate(AuthRequest authRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'authenticate'");
    }
    
}
