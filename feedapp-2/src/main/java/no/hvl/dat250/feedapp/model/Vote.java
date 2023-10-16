package no.hvl.dat250.feedapp.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="vote_info")
public class Vote {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean vote;
    private VotingPlatform platform;
    private LocalDateTime timeOfVote;


    @ManyToOne
    @JoinColumn(name = "poll_id")
    private Poll poll;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public Vote() {
    }

    public Vote(Long id, Boolean vote, VotingPlatform platform, LocalDateTime timeOfVote) {
        this.id = id;
        this.vote = vote;
        this.platform = platform;
        this.timeOfVote = timeOfVote;
    }
}
