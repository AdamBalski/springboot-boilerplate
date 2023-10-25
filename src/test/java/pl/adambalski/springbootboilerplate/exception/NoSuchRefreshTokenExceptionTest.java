package pl.adambalski.springbootboilerplate.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NoSuchRefreshTokenExceptionTest {
    @Test
    void testConstructor() {
        var noSuchRefreshTokenException = new NoSuchRefreshTokenException();
        var responseStatusException = new ResponseStatusException(HttpStatus.UNAUTHORIZED, "NO_SUCH_REFRESH_TOKEN_EXCEPTION");

        assertAll(
                () -> assertEquals(responseStatusException.getReason(), noSuchRefreshTokenException.getReason()),
                () -> assertEquals(responseStatusException.getStatus(), noSuchRefreshTokenException.getStatus())
        );
    }

}