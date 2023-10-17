package no.hvl.dat250.feedapp.DTO.authentication;

public class AuthResponseDTO {

    public String jwt; 

    public AuthResponseDTO(String jwt) {
        this.jwt = jwt;
    }
    
}
