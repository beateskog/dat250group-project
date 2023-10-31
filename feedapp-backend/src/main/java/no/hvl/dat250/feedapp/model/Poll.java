package no.hvl.dat250.feedapp.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name="poll_info")
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int pollPin;
    private String pollURL;
    private String question;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private PollPrivacy pollPrivacy;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL)
    private List<Vote> votes = new ArrayList<>();

    public Poll () {
    }

    public Poll (Long id, int pollPin, String pollURL, String question, LocalDateTime start, LocalDateTime end) {
        this.id = id;
        this.pollPin = pollPin;
        this.pollURL = pollURL;
        this.question = question;
        this.startTime = start;
        this.endTime = end;
    }
}
