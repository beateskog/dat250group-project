package no.hvl.dat250.feedapp.dto;

import java.util.List;

public class AccountDTO {

    public Long id;
    public String username;
    public String role;
    public int numberOfpolls;
    public List<Long> polls;
    
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public int getNumberOfpolls() {
        return numberOfpolls;
    }

}
