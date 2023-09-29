package no.hvl.dat250.feedapp.exeptions;

public class PollNotFoundException extends RuntimeException {
    public PollNotFoundException(String message) {
        super(message);
    }
} 
