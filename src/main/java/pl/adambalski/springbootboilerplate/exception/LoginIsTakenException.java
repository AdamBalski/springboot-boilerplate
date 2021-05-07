package pl.adambalski.springbootboilerplate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Thrown if someone wants to sign up with a login that is taken.<br><br>
 *
 * @author Adam Balski
 * @see org.springframework.web.server.ResponseStatusException
 */
public class LoginIsTakenException extends ResponseStatusException {
    public LoginIsTakenException() {
        super(HttpStatus.BAD_REQUEST, "LOGIN_IS_TAKEN_EXCEPTION");
    }
}
