package pl.adambalski.springbootboilerplate.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import pl.adambalski.springbootboilerplate.exception.AuthenticationNotAccessibleException;

/**
 * Controller to use by client for creating, updating, deleting, retrieving {@link pl.adambalski.springbootboilerplate.model.User}'s (users') data.<br><br>
 *
 * @see Controller
 * @see pl.adambalski.springbootboilerplate.model.User
 * @author Adam Balski
 */
@Controller
public interface UserController {
    Authentication getAuthentication();

    default String getUsername() throws AuthenticationNotAccessibleException {
        Authentication authentication = getAuthentication();
        if(authentication == null) {
            throw new AuthenticationNotAccessibleException();
        }

        return authentication.getName();
    }
}
