package no.hvl.dat250.feedapp.service;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;

@Service
public interface JwtService {

    public String retrieveUsername(String jwt);

    
    public Claims retrieveAllClaims(String jwt);

    public boolean isTokenExpired(String jwt);

    public boolean isTokenValid(String jwt, UserDetails userDetails);

    public String generateToken(UserDetails userDetails, Map<String,Object> claims);
    
    public String generateToken(UserDetails userDetails);
}
