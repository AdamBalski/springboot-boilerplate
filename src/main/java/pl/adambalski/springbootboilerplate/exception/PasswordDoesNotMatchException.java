package pl.adambalski.springbootboilerplate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PasswordDoesNotMatchException extends ResponseStatusException {
    public PasswordDoesNotMatchException() {
        super(HttpStatus.UNAUTHORIZED, "PASSWORD_DOES_NOT_MATCH_EXCEPTION");
    }
}
