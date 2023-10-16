package no.hvl.dat250.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import no.hvl.dat250.feedapp.service.JwtService;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
 
    private final JwtService jwtService;
	private final UserDetailsService userDetailsService;
	
	public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService) {
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request, 
			@NonNull HttpServletResponse response, 
			@NonNull FilterChain filterChain)
			throws ServletException, IOException {
		//Header that contains JWT-token
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String username;
		//Header should always start with "Bearer.."
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			//Sends request and response to next filter.
			filterChain.doFilter(request, response);
			return;
		}
		// Starts at 7 because length of "Bearer " is 7. 
		jwt = authHeader.substring(7);
		username = jwtService.retrieveUsername(jwt);
		// Checks if the user is already authenticated
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			//Get userdetails from database
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			//Check if user and token is valid.
			if(jwtService.isTokenValid(jwt, userDetails)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				//update authenticationtoken
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
			filterChain.doFilter(request, response);
		}
	}

}
