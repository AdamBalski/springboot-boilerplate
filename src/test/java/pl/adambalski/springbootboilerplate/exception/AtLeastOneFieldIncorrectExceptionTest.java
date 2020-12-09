package pl.adambalski.springbootboilerplate.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

class AtLeastOneFieldIncorrectExceptionTest {
    @Test
    void testConstructor() {
        var atLeastOneFieldIncorrectException = new AtLeastOneFieldIncorrectException();
        var responseStatusException = new ResponseStatusException(HttpStatus.BAD_REQUEST, "AT_LEAST_ONE_FIELD_IS_INCORRECT_EXCEPTION");

        assertAll(
                () -> assertEquals(responseStatusException.getReason(), atLeastOneFieldIncorrectException.getReason()),
                () -> assertEquals(responseStatusException.getStatus(), atLeastOneFieldIncorrectException.getStatus())
        );
    }

}