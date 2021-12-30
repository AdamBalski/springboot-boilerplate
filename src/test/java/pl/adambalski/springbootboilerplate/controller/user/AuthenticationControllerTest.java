package pl.adambalski.springbootboilerplate.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pl.adambalski.springbootboilerplate.dto.JwtTokenDto;
import pl.adambalski.springbootboilerplate.dto.LoginDto;
import pl.adambalski.springbootboilerplate.exception.AtLeastOneFieldIncorrectException;
import pl.adambalski.springbootboilerplate.exception.NoSuchRefreshTokenException;
import pl.adambalski.springbootboilerplate.exception.PasswordDoesNotMatchException;
import pl.adambalski.springbootboilerplate.model.RefreshToken;
import pl.adambalski.springbootboilerplate.repository.AdminRepository;
import pl.adambalski.springbootboilerplate.repository.RefreshTokenRepository;
import pl.adambalski.springbootboilerplate.repository.UserRepository;
import pl.adambalski.springbootboilerplate.security.SecurityConfiguration;
import pl.adambalski.springbootboilerplate.security.util.JwtUtil;
import pl.adambalski.springbootboilerplate.service.AuthenticationService;
import pl.adambalski.springbootboilerplate.util.RandomAlphaNumericStringGenerator;

import javax.servlet.http.Cookie;
import java.sql.Date;
import java.time.Instant;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.adambalski.springbootboilerplate.security.SecurityConfiguration.*;

@WebMvcTest
@WithAnonymousUser
public class AuthenticationControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    ApplicationContext applicationContext;

    @MockBean
    AuthenticationService authenticationService;
    @MockBean
    RandomAlphaNumericStringGenerator stringGenerator;

    // ApplicationContext wants repositories (controllers -> services -> repositories),
    // to inject to services, which are needed to be injected to controllers
    // but springboot can't instantiate repositories with @WebMvcTest, so we
    // have to create mock beans.
    @MockBean
    AdminRepository adminRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    RefreshTokenRepository refreshTokenRepository;

    JwtUtil jwtUtil;

    @BeforeEach
    void init() {
        this.jwtUtil = new JwtUtil(SecurityConfiguration.KEY);
    }


    // methods below test AuthenticationController.authenticate

    @Test
    @WithMockUser(username = "mockusername", roles = "ADMIN")
    void testRefreshIfUserLoggedInAsAdmin() throws Exception {
        testRefresh();
    }

    @Test
    @WithMockUser(username = "mockusername", roles = "USER")
    void testRefreshIfUserIsLoggedInAsUser() throws Exception {
        testRefresh();
    }

    @Test
    void testRefreshIfUserIsNotLoggedIn() throws Exception {
        testRefresh();
    }

    void testRefresh() throws Exception {
        String username = "username";
        String refreshTokenValue = "ABC123";
        JwtTokenDto jwtTokenDto = new JwtTokenDto(jwtUtil.tokenOf(username));

        when(authenticationService.refresh(username, refreshTokenValue)).thenReturn(jwtTokenDto);
        when(stringGenerator.generate()).thenReturn(refreshTokenValue);

        RefreshToken refreshToken = RefreshToken.createRefreshToken(username, stringGenerator);
        Cookie usernameCookie = createUsernameCookie(refreshToken);
        Cookie refreshTokenCookie = refreshToken.toCookie();

        var request = post("/api/auth/refresh")
                .with(csrf())
                .cookie(usernameCookie, refreshTokenCookie);

        String expectedJwtTokenDto = new ObjectMapper().writeValueAsString(jwtTokenDto);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJwtTokenDto));
    }

    @Test
    void testRefreshIfThereIsNoUsernameCookie() throws Exception {
        String username = "username";

        JwtTokenDto jwtTokenDto = new JwtTokenDto(jwtUtil.tokenOf(username));
        when(authenticationService.refresh(username, "ABC123")).thenReturn(jwtTokenDto);

        RefreshToken refreshToken = RefreshToken.createRefreshToken(username, stringGenerator);
        Cookie refreshTokenCookie = refreshToken.toCookie();

        var request = post("/api/auth/refresh")
                .cookie(refreshTokenCookie)
                .with(csrf());

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Required cookie 'username' for method parameter type String is not present"));
    }

    @Test
    void testRefreshIfThereIsNoRefreshTokenCookie() throws Exception {
        String username = "username";

        JwtTokenDto jwtTokenDto = new JwtTokenDto(jwtUtil.tokenOf(username));
        when(authenticationService.refresh(username, "ABC123")).thenReturn(jwtTokenDto);

        RefreshToken refreshToken = RefreshToken.createRefreshToken(username, stringGenerator);
        Cookie usernameCookie = createUsernameCookie(refreshToken);

        var request = post("/api/auth/refresh")
                .cookie(usernameCookie)
                .with(csrf());

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Required cookie 'refresh_token' for method parameter type String is not present"));
    }

    @Test
    void testRefreshIfThereIsNoRefreshTokenCookieAndNoUsernameCookie() throws Exception {
        String username = "username";

        JwtTokenDto jwtTokenDto = new JwtTokenDto(jwtUtil.tokenOf(username));
        when(authenticationService.refresh(username, "ABC123")).thenReturn(jwtTokenDto);

        var request = post("/api/auth/refresh")
                .with(csrf());

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Required cookie 'username' for method parameter type String is not present"));
    }

    @Test
    void testRefreshIfThereIsNoSuchTokenUsernameCombination() throws Exception {
        String username = "username";
        String refreshTokenValue = "ABC123";
        JwtTokenDto jwtTokenDto = new JwtTokenDto(jwtUtil.tokenOf(username));

        when(authenticationService.refresh(username, refreshTokenValue))
                .thenThrow(new NoSuchRefreshTokenException());
        when(stringGenerator.generate()).thenReturn(refreshTokenValue);

        RefreshToken refreshToken = RefreshToken.createRefreshToken(username, stringGenerator);
        Cookie usernameCookie = createUsernameCookie(refreshToken);
        Cookie refreshTokenCookie = refreshToken.toCookie();

        var request = post("/api/auth/refresh")
                .with(csrf())
                .cookie(usernameCookie, refreshTokenCookie);

        String expectedJwtTokenDto = new ObjectMapper().writeValueAsString(jwtTokenDto);

        mvc.perform(request)
                .andExpect(status().isUnauthorized())
                .andExpect(status().reason("NO_SUCH_REFRESH_TOKEN_EXCEPTION"));
    }

    private Cookie createUsernameCookie(RefreshToken refreshToken) {
        Cookie cookie = new Cookie(USERNAME_COOKIE_NAME, refreshToken.getUserLogin());

        int secondsToExpirationDate = (int)(refreshToken.getExpirationDate().getTime() - Instant.now().toEpochMilli()) / 1000;
        cookie.setMaxAge(secondsToExpirationDate);

        cookie.setSecure(SecurityConfiguration.COOKIE_SECURENESS);
        cookie.setHttpOnly(true);
        // "/api/auth/authenticate" and "/api/auth/get-access-token"
        cookie.setPath("/api/auth/");
        cookie.setComment("Username of the logged in user.");

        return cookie;
    }

    // methods below test Authentication.authenticate

    @Test
    @WithMockUser(username = "mockusername", roles = "ADMIN")
    void testAuthenticateIfUserLoggedInAsAdmin() throws Exception {
        LoginDto loginDto = new LoginDto("mockusername", "password");

        String loginDtoJSON = new ObjectMapper().writeValueAsString(loginDto);
        var request = post("/api/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginDtoJSON);

        mvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "mockusername", roles = "USER")
    void testAuthenticateIfUserIsLoggedInAsUser() throws Exception {
        LoginDto loginDto = new LoginDto("username", "password");

        String loginDtoJSON = new ObjectMapper().writeValueAsString(loginDto);
        var request = post("/api/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginDtoJSON);

        mvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void testAuthenticateIfEverythingIsOk() throws Exception {
        LoginDto loginDto = new LoginDto("username", "password");
        String refreshTokenCookieValue = "ABC123ABC123";
        RefreshToken refreshToken = new RefreshToken(
                1,
                loginDto.username(),
                refreshTokenCookieValue,
                new Date(Integer.MAX_VALUE)
        );
        long expirationDate = refreshToken.getExpirationDate().getTime();
        int maxAge = (int)(expirationDate - Instant.now().toEpochMilli()) / 1000;;

        // mocking
        when(stringGenerator.generate())
                .thenReturn(refreshTokenCookieValue);
        when(authenticationService.authenticate(loginDto))
                .thenReturn(refreshToken);

        // creating a request
        String loginDtoJSON = new ObjectMapper().writeValueAsString(loginDto);
        var request = post("/api/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginDtoJSON);

        // performing the request and testing the response
        mvc.perform(request)
                .andExpect(status().isOk())
                // 'refresh_token' cookie
                .andExpect(cookie().exists(REFRESH_TOKEN_COOKIE_NAME))
                .andExpect(cookie().path(REFRESH_TOKEN_COOKIE_NAME, "/api/auth/"))
                .andExpect(cookie().value(REFRESH_TOKEN_COOKIE_NAME, refreshTokenCookieValue))
                .andExpect(cookie().comment(REFRESH_TOKEN_COOKIE_NAME, "The refresh token is used to make sure the client is log on, when getting the access token."))
                .andExpect(cookie().secure(REFRESH_TOKEN_COOKIE_NAME, COOKIE_SECURENESS))
                .andExpect(cookie().maxAge(REFRESH_TOKEN_COOKIE_NAME, maxAge))
                // 'username' cookie
                .andExpect(cookie().exists(USERNAME_COOKIE_NAME))
                .andExpect(cookie().exists(loginDto.username()))
                .andExpect(cookie().comment(USERNAME_COOKIE_NAME, "Username of the logged in user."))
                .andExpect(cookie().httpOnly(USERNAME_COOKIE_NAME, true))
                .andExpect(cookie().secure(USERNAME_COOKIE_NAME, COOKIE_SECURENESS))
                .andExpect(cookie().maxAge(REFRESH_TOKEN_COOKIE_NAME, maxAge));

        // mock some things, it should theoretically work
    }

    @Test
    @WithAnonymousUser
    void testAuthenticateIfNoSuchUser() throws Exception {
        LoginDto loginDto = new LoginDto("nonexistent_username", "password");

        when(authenticationService.authenticate(loginDto))
                .thenThrow(UsernameNotFoundException.class);

        String loginDtoJSON = new ObjectMapper().writeValueAsString(loginDto);
        var request = post("/api/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginDtoJSON);

        mvc.perform(request)
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithAnonymousUser
    void testAuthenticateIfPasswordDoesNotMatch() throws Exception {
        LoginDto loginDto = new LoginDto(null, "invalid_password");

        when(authenticationService.authenticate(loginDto))
                .thenThrow(new PasswordDoesNotMatchException());

        String loginDtoJSON = new ObjectMapper().writeValueAsString(loginDto);
        var request = post("/api/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginDtoJSON);

        mvc.perform(request)
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithAnonymousUser
    void testAuthenticateIfUsernameIsNull() throws Exception {
        LoginDto loginDto = new LoginDto(null, "password");

        when(authenticationService.authenticate(loginDto))
                .thenThrow(new AtLeastOneFieldIncorrectException());

        String loginDtoJSON = new ObjectMapper().writeValueAsString(loginDto);
        var request = post("/api/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginDtoJSON);

        mvc.perform(request)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(status().reason("AT_LEAST_ONE_FIELD_INCORRECT_EXCEPTION"));
    }
}
