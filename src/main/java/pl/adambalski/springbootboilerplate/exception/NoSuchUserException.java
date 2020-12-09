package pl.adambalski.springbootboilerplate.exception;

/**
 * Thrown if there is a call for certain user, but the user does not exist.<br><br>
 *
 * @author Adam Balski
 * @see IllegalStateException
 */
public class NoSuchUserException extends IllegalStateException {
    public NoSuchUserException() {
        this("There is no such user.");
    }

    public NoSuchUserException(String message) {
        super(message);
    }
}
