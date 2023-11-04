package no.hvl.dat250.feedapp.service;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import io.jsonwebtoken.Claims;


public interface JwtService {

    /**
     * Retrieves the username from the given jwt
     * @param jwt the jwt to retrieve the username from
     * @return the username
     */
    public String retrieveUsername(String jwt);

    /**
     * Retrieves all claims from the given jwt
     * @param jwt the jwt to retrieve the claims from
     * @return the claims
     */
    public Claims retrieveAllClaims(String jwt);

    /**
     * Checks if the given jwt is expired
     * @param jwt the jwt to check
     * @return true if the jwt is expired, false otherwise
     */
    public boolean isTokenExpired(String jwt);

    /**
     * Checks if the given jwt is valid
     * @param jwt the jwt to check
     * @param userDetails the user details to check
     * @return true if the jwt is valid, false otherwise
     */
    public boolean isTokenValid(String jwt, UserDetails userDetails);

    /**
     * Generates a jwt with the given user details and claims
     * @param userDetails the user details to use
     * @param claims the claims to use
     * @return the generated jwt
     */
    public String generateToken(UserDetails userDetails, Map<String,Object> claims);
    
    /**
     * Generates a jwt with the given user details
     * @param userDetails the user details to use
     * @return the generated jwt
     */
    public String generateToken(UserDetails userDetails);
}
