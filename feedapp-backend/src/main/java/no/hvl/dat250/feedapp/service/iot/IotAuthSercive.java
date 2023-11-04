package no.hvl.dat250.feedapp.service.iot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat250.feedapp.security.ApplicationConfig;

@Service
public class IotAuthSercive {

    @Autowired
    ApplicationConfig applicationConfig;

    /**
     * Checks if the given api key is valid
     * @param apiKey
     * @return true if the api key is valid, false otherwise
     */
    public boolean isValidApiKey(String apiKey) {
        return (applicationConfig.getApiKey().equals(apiKey)); 
            
    }
    
}
