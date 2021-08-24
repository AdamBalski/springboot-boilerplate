package pl.adambalski.springbootboilerplate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoSuchRefreshTokenException extends ResponseStatusException {
    public NoSuchRefreshTokenException() {
        super(HttpStatus.UNAUTHORIZED, "NO_SUCH_REFRESH_TOKEN_EXCEPTION");
    }
}
