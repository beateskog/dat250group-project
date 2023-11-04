package no.hvl.dat250.feedapp.dto.authentication;

/**
 * Data transfer object for authentication responses
 * Used to transfer authentication responses between the frontend and backend
 */
public class AuthResponseDTO {

    public String jwt; 

    public AuthResponseDTO(String jwt) {
        this.jwt = jwt;
    }

    public AuthResponseDTO() {
    }

    public void setjwt(String jwt) {
        this.jwt = jwt;
    }

    public String getjwt() { 
        return jwt; }
    
}
