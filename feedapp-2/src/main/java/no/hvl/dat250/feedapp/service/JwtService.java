package no.hvl.dat250.feedapp.service;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;


public interface JwtService {

    public String retrieveUsername(String jwt);

    
    public Claims retrieveAllClaims(String jwt);

    public boolean isTokenExpired(String jwt);

    public boolean isTokenValid(String jwt, UserDetails userDetails);

    public String generateToken(UserDetails userDetails, Map<String,Object> claims);
    
    public String generateToken(UserDetails userDetails);
}
