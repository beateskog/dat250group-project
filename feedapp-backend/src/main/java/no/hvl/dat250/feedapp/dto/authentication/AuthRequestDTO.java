package no.hvl.dat250.feedapp.dto.authentication;

/**
 * Data transfer object for authentication requests
 * Used to transfer authentication requests between the frontend and backend
 */
public class AuthRequestDTO {

    private String username;
    private String password;

    public AuthRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AuthRequestDTO() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() { 
        return username; }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() { 
        return password; }
}
