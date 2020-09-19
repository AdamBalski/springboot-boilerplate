package pl.adambalski.springbootboilerplate.exception;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Thrown when SecurityContextHolder.getContext().getAuthentication() returns null when it should not.<br><br>
 *
 * @see org.springframework.security.core.context.SecurityContextHolder
 * @see org.springframework.security.core.context.SecurityContext
 * @see SecurityContextHolder#getContext()
 * @see SecurityContext#getAuthentication()
 * @see org.springframework.security.core.Authentication
 * @author Adam Balski
 */
public class AuthenticationNotAccessibleException extends RuntimeException {
    public AuthenticationNotAccessibleException() {
        super("SecurityContextHolder.getContext().getAuthentication() returns null, but it should not");
    }
}
