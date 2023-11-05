package no.hvl.dat250.feedapp.dto;

import java.util.List;

import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.model.Poll;

/**
 * Data transfer object for accounts
 * Used to transfer account information between the frontend and backend
 */
public class AccountDTO {

    public Long id;
    public String username;
    public String role;
    public int numberOfpolls;
    public List<Long> polls;

    public static AccountDTO AccountToAccountDTO (Account account) {

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.id = account.getId();
        accountDTO.username = account.getUsername();
        accountDTO.role = account.getRole().toString();
        accountDTO.numberOfpolls = account.getPolls().size();
        accountDTO.polls = new java.util.ArrayList<Long>();
        for (Poll poll : account.getPolls()) {
            accountDTO.polls.add(poll.getId());
        }

        return accountDTO;
    }
    
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
