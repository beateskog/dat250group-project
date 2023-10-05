package no.hvl.dat250.feedapp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private Account pollOwner;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "poll_vote",
            joinColumns = @JoinColumn(name = "poll_id"),
            inverseJoinColumns = @JoinColumn(name = "vote_id"))
    private List<Vote> votes = new ArrayList<>();

    @Override
    public String toString() {
        return String.format(
            "Poll[id=%d, pollPin='%d', question='%s', pollUrl='%s', startTime='%s', endTime='%s']",
            id, pollPin, question, pollUrl, startTime, endTime);
    }
}
