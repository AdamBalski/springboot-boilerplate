package pl.adambalski.springbootboilerplate.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.adambalski.springbootboilerplate.dto.JwtTokenDto;
import pl.adambalski.springbootboilerplate.dto.LoginDto;
import pl.adambalski.springbootboilerplate.model.RefreshToken;
import pl.adambalski.springbootboilerplate.security.SecurityConfiguration;
import pl.adambalski.springbootboilerplate.security.util.JwtUtil;
import pl.adambalski.springbootboilerplate.service.AuthenticationService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.time.Instant;

import static pl.adambalski.springbootboilerplate.security.SecurityConfiguration.REFRESH_TOKEN_COOKIE_NAME;
import static pl.adambalski.springbootboilerplate.security.SecurityConfiguration.USERNAME_COOKIE_NAME;

/**
 * Contains a function responsible for authenticating a user, which returns a jwt token.<br><br>
 *
 * @see SecurityConfiguration
 * @see UserDetailsService
 * @see UserDetails
 * @see JwtUtil
 * @see PasswordEncoder
 * @author Adam Balski
 */

@ComponentScan("pl.adambalski.springbootboilerplate")
@RestController
@CrossOrigin
public class AuthenticationController {
    AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "/api/auth/authenticate")
    @PreAuthorize(value = "isAnonymous()")
    public void authenticate(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        RefreshToken refreshToken = authenticationService.authenticate(loginDto);

        response.addCookie(refreshToken.toCookie());
        response.addCookie(createUsernameCookie(loginDto.username(), refreshToken.getExpirationDate()));
    }

    @PostMapping(value = "/api/auth/refresh")
    @PreAuthorize(value = "permitAll()")
    public JwtTokenDto refresh(@CookieValue(USERNAME_COOKIE_NAME) String username,
                               @CookieValue(REFRESH_TOKEN_COOKIE_NAME) String refreshTokenValue) {
         return authenticationService.refresh(username, refreshTokenValue);
    }

    private Cookie createUsernameCookie(String username, Date expirationDate) {
        Cookie cookie = new Cookie(USERNAME_COOKIE_NAME, username);

        int secondsToExpirationDate = (int)(expirationDate.getTime() - Instant.now().toEpochMilli()) / 1000;
        cookie.setMaxAge(secondsToExpirationDate);

        cookie.setSecure(SecurityConfiguration.COOKIE_SECURENESS);
        cookie.setHttpOnly(true);
        // "/api/auth/authenticate" and "/api/auth/get-access-token"
        cookie.setPath("/api/auth/");
        cookie.setComment("Username of the logged in user.");

        return cookie;
    }
}
