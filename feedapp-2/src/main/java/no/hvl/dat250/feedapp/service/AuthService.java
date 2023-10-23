package no.hvl.dat250.feedapp.service;


import no.hvl.dat250.feedapp.DTO.authentication.AuthRequestDTO;
import no.hvl.dat250.feedapp.DTO.authentication.AuthResponseDTO;
import no.hvl.dat250.feedapp.DTO.authentication.RegisterRequestDTO;



public interface AuthService {

    public AuthResponseDTO register(RegisterRequestDTO registerRequest);
    
    public AuthResponseDTO authenticate(AuthRequestDTO authRequest);
}
