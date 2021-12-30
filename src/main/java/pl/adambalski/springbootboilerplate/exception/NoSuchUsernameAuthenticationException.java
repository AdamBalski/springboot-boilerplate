package pl.adambalski.springbootboilerplate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;



/**
 * Thrown if a user tries to authenticate, but their username doesn't exist.<br>
 *
 * @author Adam Balski
 * @see org.springframework.web.server.ResponseStatusException
 * @see pl.adambalski.springbootboilerplate.controller.user.AuthenticationController
 * @see pl.adambalski.springbootboilerplate.service.AuthenticationService
 */
public class NoSuchUsernameAuthenticationException extends ResponseStatusException {
    public NoSuchUsernameAuthenticationException() {
        super(HttpStatus.UNAUTHORIZED, "NO_SUCH_USERNAME_AUTHENTICATION_EXCEPTION");
    }
}
