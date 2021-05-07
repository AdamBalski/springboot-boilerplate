package pl.adambalski.springbootboilerplate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Thrown if someone wants to sign up with an email that is taken.<br><br>
 *
 * @author Adam Balski
 * @see org.springframework.web.server.ResponseStatusException
 */
public class EmailIsTakenException extends ResponseStatusException {
    public EmailIsTakenException() {
        super(HttpStatus.BAD_REQUEST, "EMAIL_IS_TAKEN_EXCEPTION");
    }
}
