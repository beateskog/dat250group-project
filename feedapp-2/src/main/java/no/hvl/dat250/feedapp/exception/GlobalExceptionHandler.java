package no.hvl.dat250.feedapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // Responsible to handle exceptions globally throughout this project.
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {

        ExceptionDetails notFoundException = new ExceptionDetails(
            ex.getMessage(),
            ex.getCause(),
            HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(notFoundException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {

        ExceptionDetails badRequestException = new ExceptionDetails(
            ex.getMessage(),
            ex.getCause(),
            HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(badRequestException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<Object> handleInternalServerErrorException(InternalServerErrorException ex) {

        ExceptionDetails internalServerErrorException = new ExceptionDetails(
            ex.getMessage(),
            ex.getCause(),
            HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(internalServerErrorException, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
