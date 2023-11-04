package no.hvl.dat250.feedapp.service;


import no.hvl.dat250.feedapp.dto.authentication.AuthRequestDTO;
import no.hvl.dat250.feedapp.dto.authentication.AuthResponseDTO;
import no.hvl.dat250.feedapp.dto.authentication.RegisterRequestDTO;
import no.hvl.dat250.feedapp.exception.AccessDeniedException;
import no.hvl.dat250.feedapp.exception.BadRequestException;



public interface AuthService {

    /**
     * Registers a new user and saves it to the database.
     * @param registerRequest The registerRequestDTO for the user to be created.
     * @return An authResponseDTO containing the JWT for the created user.
     * @throws BadRequestException If the username or password is null or blank.
     * @throws BadRequestException If the username already exists.
     */
    public AuthResponseDTO register(RegisterRequestDTO registerRequest);
    
    /**
     * Authenticates a user and returns a JWT if the user exists and the password is correct.
     * @param authRequest The authRequestDTO containing the username and password of the user to be authenticated.
     * @return An authResponseDTO containing the JWT for the authenticated user.
     * @throws AccessDeniedException If the user does not exist or the password is incorrect.
     */
    public AuthResponseDTO authenticate(AuthRequestDTO authRequest);
}
