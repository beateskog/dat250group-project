package no.hvl.dat250.security;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    AuthenticationProvider authProvider;

    @Autowired
    JwtAuthFilter jwtAuthFilter;

    @Bean 
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
			.csrf()
			.disable()
			.authorizeHttpRequests()
			.requestMatchers("/**").permitAll()
			.anyRequest()
			.authenticated()
			.and() // "and()" to add a new configuration.
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS) //New session for each request
			.and()
			.authenticationProvider(authProvider)
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
        
    }
    
}
