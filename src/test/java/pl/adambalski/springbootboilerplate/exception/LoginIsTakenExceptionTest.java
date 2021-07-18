package pl.adambalski.springbootboilerplate.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

class LoginIsTakenExceptionTest {
    @Test
    void testConstructor() {
        var loginIsTakenException = new LoginIsTakenException();
        var responseStatusException = new ResponseStatusException(HttpStatus.CONFLICT, "LOGIN_IS_TAKEN_EXCEPTION");

        assertAll(
                () -> assertEquals(responseStatusException.getReason(), loginIsTakenException.getReason()),
                () -> assertEquals(responseStatusException.getStatus(), loginIsTakenException.getStatus())
        );
    }
}