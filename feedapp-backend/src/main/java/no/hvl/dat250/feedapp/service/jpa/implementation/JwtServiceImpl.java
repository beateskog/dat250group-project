package no.hvl.dat250.feedapp.service.jpa.implementation;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import no.hvl.dat250.feedapp.service.JwtService;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${app.secret_key}")
    private String SECRET_KEY;

    public void setSecretKey(String secretKey) {
        this.SECRET_KEY = secretKey;
    }
    
    @Override
    public String retrieveUsername(String jwt) {
        return retrieveAllClaims(jwt).getSubject();
    }

    @Override
    public Claims retrieveAllClaims(String jwt) {
        return Jwts.parserBuilder()
            .setSigningKey(getKey())
            .build()
            .parseClaimsJws(jwt)
            .getBody();
    }

    @Override
    public boolean isTokenExpired(String jwt) {
        return retrieveAllClaims(jwt).getExpiration().before(new java.util.Date());
    }

    @Override
    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        final String username = retrieveUsername(jwt);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(jwt);
    }

    @Override
    public String generateToken(UserDetails userDetails, Map<String, Object> claims) {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            //12 hours
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60* 12))
            .signWith(getKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    @Override
    public String generateToken(UserDetails userDetails) {
		return generateToken(userDetails, new HashMap<>());
	}
    
    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);

    }
}
