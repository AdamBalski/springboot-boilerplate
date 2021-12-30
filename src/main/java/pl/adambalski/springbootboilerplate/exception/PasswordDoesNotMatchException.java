package pl.adambalski.springbootboilerplate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Thrown if a user tries to authenticate, but their password does not match their username.<br>
 *
 * @author Adam Balski
 * @see org.springframework.web.server.ResponseStatusException
 * @see pl.adambalski.springbootboilerplate.controller.user.AuthenticationController
 * @see pl.adambalski.springbootboilerplate.service.AuthenticationService
 */
public class PasswordDoesNotMatchException extends ResponseStatusException {
    public PasswordDoesNotMatchException() {
        super(HttpStatus.UNAUTHORIZED, "PASSWORD_DOES_NOT_MATCH_EXCEPTION");
    }
}
