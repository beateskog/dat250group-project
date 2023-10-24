package no.hvl.dat250.feedapp.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import no.hvl.dat250.feedapp.service.jpa.JwtService;

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

		final String authHeader = request.getHeader("Authorization");
		final String username;
		final String jwt;
		
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		try {
			jwt = authHeader.substring(7);
			username = jwtService.retrieveUsername(jwt);
		
		
			if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			try {
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
				if(jwtService.isTokenValid(jwt, userDetails)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					
					
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			} catch (UsernameNotFoundException e) {
				logger.info("User not found: " + username);
			}
			filterChain.doFilter(request, response);
		}
		} catch (Exception e) {
			filterChain.doFilter(request, response);
			logger.error("Error while authenticating user: " + e.getMessage());
		}
	}

}
