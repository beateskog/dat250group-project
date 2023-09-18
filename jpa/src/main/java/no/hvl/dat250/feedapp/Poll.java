package no.hvl.dat250.feedapp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Poll {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int pollPin;
    private String question;
    private String pollUrl;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ManyToOne
    private Account pollOwner;

    @OneToMany
    @JoinTable(name = "poll_vote",
            joinColumns = @JoinColumn(name = "poll_id"),
            inverseJoinColumns = @JoinColumn(name = "vote_id"))
    private List<Vote> votes = new ArrayList<>();
}
