package pl.adambalski.springbootboilerplate.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.adambalski.springbootboilerplate.dto.SignUpUserDto;
import pl.adambalski.springbootboilerplate.model.User;
import pl.adambalski.springbootboilerplate.service.UserService;

/**
 * Provides a simple API for users to make simple operations like getting their data,
 * deleting their account, signing up and so on.<br><br>
 *
 * @see RestController
 * @see UserService
 * @author Adam Balski
 */
@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/api/user/get-data")
    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    public User getOwnData() {
        User user = userService.getUserByLogin(getUsername());
        removeUuidAndPasswordInformation(user);
        return user;
    }

    @DeleteMapping(value = "/api/user/delete-logged-user")
    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    public void deleteLoggedUser() {
        userService.deleteUserByLogin(getUsername());
    }

    @PutMapping(value = "/api/user/sign-up")
    @PreAuthorize(value = "isAnonymous()")
    public void signUp(@RequestBody SignUpUserDto signUpUserDto) {
        userService.addSignUpUserDto(signUpUserDto);
    }

    private String getUsername() {
        return getAuthentication().getName();
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private void removeUuidAndPasswordInformation(User user) {
        user.setUuid(null);
        user.setPassword(null);
    }
}
