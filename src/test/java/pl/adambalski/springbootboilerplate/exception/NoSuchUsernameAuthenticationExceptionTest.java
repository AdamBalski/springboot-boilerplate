package pl.adambalski.springbootboilerplate.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

class NoSuchUsernameAuthenticationExceptionTest {
    @Test
    void testConstructor() {
        var noSuchUsernameAuthenticationException = new NoSuchUsernameAuthenticationException();
        var responseStatusException = new ResponseStatusException(HttpStatus.UNAUTHORIZED, "NO_SUCH_USERNAME_AUTHENTICATION_EXCEPTION");

        assertAll(
                () -> assertEquals(responseStatusException.getReason(), noSuchUsernameAuthenticationException.getReason()),
                () -> assertEquals(responseStatusException.getStatus(), noSuchUsernameAuthenticationException.getStatus())
        );
    }
}