package pl.adambalski.springbootboilerplate.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

class PasswordDoesNotMatchExceptionTest {
    @Test
    void testConstructor() {
        var passwordDoesNotMatchException = new PasswordDoesNotMatchException();
        var responseStatusException = new ResponseStatusException(HttpStatus.UNAUTHORIZED, "PASSWORD_DOES_NOT_MATCH_EXCEPTION");

        assertAll(
                () -> assertEquals(responseStatusException.getReason(), passwordDoesNotMatchException.getReason()),
                () -> assertEquals(responseStatusException.getStatus(), passwordDoesNotMatchException.getStatus())
        );
    }
}