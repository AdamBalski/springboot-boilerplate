package pl.adambalski.springbootboilerplate.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

class EmailIsTakenExceptionTest {
    @Test
    void testConstructor() {
        var emailIsTakenException = new EmailIsTakenException();
        var responseStatusException = new ResponseStatusException(HttpStatus.CONFLICT, "EMAIL_IS_TAKEN_EXCEPTION");

        assertAll(
                () -> assertEquals(responseStatusException.getReason(), emailIsTakenException.getReason()),
                () -> assertEquals(responseStatusException.getStatus(), emailIsTakenException.getStatus())
        );
    }
}