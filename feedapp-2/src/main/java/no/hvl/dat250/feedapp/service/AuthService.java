package no.hvl.dat250.feedapp.service;

import no.hvl.dat250.feedapp.DTO.AuthResponse;
import no.hvl.dat250.feedapp.authentication.AuthRequest;
import no.hvl.dat250.feedapp.authentication.RegisterRequest;

public interface AuthService {

    public AuthResponse register(RegisterRequest registerRequest);
    
    public AuthResponse authenticate(AuthRequest authRequest);
}
