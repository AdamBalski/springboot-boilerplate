package pl.adambalski.springbootboilerplate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Thrown if there is a call for certain user, but the user does not exist.<br>
 * For example if you try to get a certain user's data using {@link pl.adambalski.springbootboilerplate.service.AdminService}
 * and then you would use 'User getUserByUuid(UUID)'. If the user didn't exist, then the function should throw {@link NoSuchUserException}.<br><br>
 *
 * @author Adam Balski
 * @see org.springframework.web.server.ResponseStatusException
 */
public final class NoSuchUserException extends ResponseStatusException {
    public NoSuchUserException() {
        super(HttpStatus.BAD_REQUEST, "NO_SUCH_USER_EXCEPTION");
    }
}
