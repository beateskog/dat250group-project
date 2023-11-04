package no.hvl.dat250.feedapp.dto.authentication;

/**
 * Data transfer object for account responses
 * Used to transfer account responses between the frontend and backend
 */
public class AccountRespDTO {

    private Long id;
    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
    
}
