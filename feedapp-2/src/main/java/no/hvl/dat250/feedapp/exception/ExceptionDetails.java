package no.hvl.dat250.feedapp.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ExceptionDetails {
    private final String message; // The message that will be passed to client will be here
    private final Throwable throwable;
    private final HttpStatus httpStatus;

    public ExceptionDetails(String message, Throwable throwable, HttpStatus httpStatus) {
        this.message = message;
        this.throwable = throwable;
        this.httpStatus = httpStatus;
    }
}
