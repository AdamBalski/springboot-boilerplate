package pl.adambalski.springbootboilerplate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Thrown if a user tries to refresh their JWT access token, but there is no such refresh token.<br>
 *
 * @author Adam Balski
 * @see org.springframework.web.server.ResponseStatusException
 * @see pl.adambalski.springbootboilerplate.controller.user.AuthenticationController
 * @see pl.adambalski.springbootboilerplate.service.AuthenticationService
 */
public class NoSuchRefreshTokenException extends ResponseStatusException {
    public NoSuchRefreshTokenException() {
        super(HttpStatus.UNAUTHORIZED, "NO_SUCH_REFRESH_TOKEN_EXCEPTION");
    }
}
