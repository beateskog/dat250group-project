package no.hvl.dat250.feedapp.dto;

/**
 * Data transfer object for updating accounts
 * Used to transfer account updates between the frontend and backend
 */
public class UpdateAccountDTO {

    String username;
    String password;

    public UpdateAccountDTO() {}

    public UpdateAccountDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setUsername(String username) { 
        this.username = username; 
    }

    public String getUsername() { 
        return username; 
    }

    public void setPassword(String password) { 
        this.password = password; 
    }

    public String getPassword() { 
        return password; 
    }

    
}
