package pl.adambalski.springbootboilerplate.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

/**
 * Basic rest implementation of {@link UserController}<br><br>
 *
 * @see UserController
 * @see RestController
 * @author Adam Balski
 */
@RestController
public class RestUserController implements UserController {
    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
