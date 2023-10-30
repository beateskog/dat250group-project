package no.hvl.dat250.feedapp.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// A class to allow Cross-Origin Resource Sharing (CORS)
// to enable communication between different domains.
// In our case, communication between Angular (frontend) and the Spring Boot application (backend)

@Configuration 
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins("http://localhost:4200") // Angular URL
        .allowedMethods("GET", "POST", "PUT", "DELETE")
        .allowedHeaders("*");
    }
}
