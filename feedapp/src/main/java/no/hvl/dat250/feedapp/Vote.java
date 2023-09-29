package no.hvl.dat250.feedapp;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Vote {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean vote;

    @Enumerated(EnumType.STRING)
    private VotingPlatform votingPlatform;
    private LocalDateTime voteTime;

    @ManyToOne
    private Poll poll;

    @Override
    public String toString() {
        return String.format(
            "Vote[id=%d, vote='%b', votingPlatform='%s', voteTime='%s']",
            id, vote, votingPlatform, voteTime);
    }

}
