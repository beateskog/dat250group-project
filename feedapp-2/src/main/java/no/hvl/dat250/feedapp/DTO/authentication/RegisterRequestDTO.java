package no.hvl.dat250.feedapp.dto.authentication;

public class RegisterRequestDTO {

    private String username;
    private String password;

    public RegisterRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public RegisterRequestDTO() {
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
