package pl.adambalski.springbootboilerplate.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

class NoSuchUserExceptionTest {
    @Test
    void testConstructor() {
        var noSuchUserException = new NoSuchUserException();
        var responseStatusException = new ResponseStatusException(HttpStatus.NOT_FOUND, "NO_SUCH_USER_EXCEPTION");

        assertAll(
                () -> assertEquals(responseStatusException.getReason(), noSuchUserException.getReason()),
                () -> assertEquals(responseStatusException.getStatus(), noSuchUserException.getStatus())
        );
    }
}