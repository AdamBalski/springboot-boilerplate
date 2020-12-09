package pl.adambalski.springbootboilerplate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AtLeastOneFieldIncorrectException extends ResponseStatusException {
    public AtLeastOneFieldIncorrectException() {
        super(HttpStatus.BAD_REQUEST, "AT_LEAST_ONE_FIELD_IS_INCORRECT_EXCEPTION");
    }
}
