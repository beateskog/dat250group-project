package no.hvl.dat250.feedapp.exception;

public class UnauthorizedAcccessException extends RuntimeException {

    public UnauthorizedAcccessException(String message) {
        super(message);
    }

    public UnauthorizedAcccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
