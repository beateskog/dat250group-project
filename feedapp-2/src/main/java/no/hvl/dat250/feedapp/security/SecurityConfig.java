package no.hvl.dat250.feedapp.security;



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

    /*
     * This method configures the security filter chain.
     * It is responsible for authenticating the user and checking if the user has the correct permissions.
     * It also adds the jwtAuthFilter to the filter chain.
     */
    @Bean 
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
			.csrf()
            .disable()
			.authorizeHttpRequests()
            // update here based on what should be back authentication or not
			.requestMatchers("/**").permitAll()
			.anyRequest()
            .authenticated()
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authenticationProvider(authProvider)
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
        
    }
    
}
