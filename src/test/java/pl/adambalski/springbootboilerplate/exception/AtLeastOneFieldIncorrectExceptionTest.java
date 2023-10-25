package pl.adambalski.springbootboilerplate.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AtLeastOneFieldIncorrectExceptionTest {
    @Test
    void testConstructor() {
        var atLeastOneFieldIncorrectException = new AtLeastOneFieldIncorrectException();
        var responseStatusException = new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "AT_LEAST_ONE_FIELD_INCORRECT_EXCEPTION");

        assertAll(
                () -> assertEquals(responseStatusException.getReason(), atLeastOneFieldIncorrectException.getReason()),
                () -> assertEquals(responseStatusException.getStatus(), atLeastOneFieldIncorrectException.getStatus())
        );
    }

}