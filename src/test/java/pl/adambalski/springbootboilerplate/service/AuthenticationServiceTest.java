package pl.adambalski.springbootboilerplate.service;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;
import pl.adambalski.springbootboilerplate.dto.JwtTokenDto;
import pl.adambalski.springbootboilerplate.dto.LoginDto;
import pl.adambalski.springbootboilerplate.exception.AtLeastOneFieldIncorrectException;
import pl.adambalski.springbootboilerplate.exception.NoSuchRefreshTokenException;
import pl.adambalski.springbootboilerplate.exception.NoSuchUsernameAuthenticationException;
import pl.adambalski.springbootboilerplate.exception.PasswordDoesNotMatchException;
import pl.adambalski.springbootboilerplate.logger.Logger;
import pl.adambalski.springbootboilerplate.logger.Status;
import pl.adambalski.springbootboilerplate.model.RefreshToken;
import pl.adambalski.springbootboilerplate.repository.RefreshTokenRepository;
import pl.adambalski.springbootboilerplate.security.PasswordEncoderFactory;
import pl.adambalski.springbootboilerplate.security.SecurityConfiguration;
import pl.adambalski.springbootboilerplate.security.util.JwtUtil;
import pl.adambalski.springbootboilerplate.util.RandomAlphaNumericStringGenerator;

import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {
    AuthenticationService authenticationService;
    RefreshTokenRepository refreshTokenRepository;

    Logger logger;

    @BeforeEach
    void init() {
        refreshTokenRepository = mock(RefreshTokenRepository.class);
        PasswordEncoder passwordEncoder = new PasswordEncoderFactory().passwordEncoderBean();
        UserDetailsService userDetailsService = createUserDetailsService(passwordEncoder);
        RandomAlphaNumericStringGenerator randomAlphaNumericStringGenerator = createGenerator();
        logger = Mockito.mock(Logger.class);

        authenticationService = new AuthenticationService(refreshTokenRepository,
                userDetailsService,
                passwordEncoder,
                randomAlphaNumericStringGenerator,
                logger);
    }

    private UserDetailsService createUserDetailsService(PasswordEncoder passwordEncoder) {
        String encodedPassword = passwordEncoder.encode("password");

        UserDetails userDetails = new User("username", encodedPassword, List.of());

        return new MockUserDetailsService(userDetails);
    }

    private RandomAlphaNumericStringGenerator createGenerator() {
        var generator = Mockito.mock(RandomAlphaNumericStringGenerator.class);

        ReflectionTestUtils.setField(generator, "length", SecurityConfiguration.REFRESH_TOKEN_LENGTH);

        when(generator.generate(12)).thenReturn("ABCDEF123456");
        when(generator.generate()).thenCallRealMethod();

        return generator;
    }

    @Test
    void testDeleteAllExpired() {
        authenticationService.deleteAllExpired();

        verify(logger).log("DELETED ALL EXPIRED", AuthenticationService.class, Status.INFO);
        verify(refreshTokenRepository).deleteAllByExpirationDateBeforeNow();
    }

    @Test
    void testAuthenticate() {
        LoginDto loginDto = new LoginDto("username", "password");

        Period expirationPeriod = SecurityConfiguration.REFRESH_TOKEN_EXPIRATION_PERIOD;
        Date expirationDate = new Date(Instant.now().plus(expirationPeriod).toEpochMilli());

        RefreshToken expectedRefreshToken = new RefreshToken(0, "username", "ABCDEF123456", expirationDate);

        authenticationService.authenticate(loginDto);

        // checks that was invoked and
        // checks that every field is the same as expected
        // (for java.sql.Date uses toString(), because the dates differ by a few ms)
        verify(refreshTokenRepository).save(argThat(actual ->
                expectedRefreshToken.getId().equals(actual.getId()) &&
                expectedRefreshToken.getToken().equals(actual.getToken()) &&
                expectedRefreshToken.getUserLogin().equals(actual.getUserLogin()) &&
                expectedRefreshToken.getExpirationDate().toString().equals(actual.getExpirationDate().toString())
        ));
    }

    @Test
    void testAuthenticateIfPasswordIsNull() {
        LoginDto loginDto = new LoginDto("username", null);

        assertAtLeastOneFieldIncorrectWhenAuthenticating(loginDto);
    }

    @Test
    void testAuthenticateIfUsernameIsNull() {
        LoginDto loginDto = new LoginDto(null, "password");

        assertAtLeastOneFieldIncorrectWhenAuthenticating(loginDto);
    }

    @Test
    void testAuthenticateIfUsernameAndPasswordAreNull() {
        LoginDto loginDto = new LoginDto(null, null);

        assertAtLeastOneFieldIncorrectWhenAuthenticating(loginDto);
    }

    @Test
    void testAuthenticateIfPasswordIsNullAndUsernameDoesNotExist() {
        LoginDto loginDto = new LoginDto("nonexistent username", null);

        assertAtLeastOneFieldIncorrectWhenAuthenticating(loginDto);
    }

    @Test
    void testAuthenticateIfUserDoesNotExist() {
        LoginDto loginDto = new LoginDto("nonexistent user", "password123");

        assertNoSuchUserWhenAuthenticating(loginDto);
    }

    @Test
    void testAuthenticateIfPasswordDoesNotMatch() {
        LoginDto loginDto = new LoginDto("username", "non-matching password");

        assertPasswordDoesNotMatchWhenAuthenticating(loginDto);
    }

    private void assertNoSuchUserWhenAuthenticating(LoginDto loginDto) {
        Executable executable = () -> authenticationService.authenticate(loginDto);

        ResponseStatusException exception =
                assertThrows(NoSuchUsernameAuthenticationException.class, executable);
        assertEquals("NO_SUCH_USERNAME_AUTHENTICATION_EXCEPTION", exception.getReason());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
    }

    private void assertAtLeastOneFieldIncorrectWhenAuthenticating(LoginDto loginDto) {
        Executable executable = () -> authenticationService.authenticate(loginDto);

        ResponseStatusException exception =
                assertThrows(AtLeastOneFieldIncorrectException.class, executable);
        assertEquals("AT_LEAST_ONE_FIELD_INCORRECT_EXCEPTION", exception.getReason());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatus());
    }

    private void assertPasswordDoesNotMatchWhenAuthenticating(LoginDto loginDto) {
        Executable executable = () -> authenticationService.authenticate(loginDto);

        ResponseStatusException exception =
                assertThrows(PasswordDoesNotMatchException.class, executable);
        assertEquals("PASSWORD_DOES_NOT_MATCH_EXCEPTION", exception.getReason());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
    }

    @Test
    void testRefreshIfDoesNotExistByRefreshTokenRepository() {
        String username = "username";
        String refreshToken = "ABC123";

        when(refreshTokenRepository.existsByUserLoginAndToken(username, refreshToken)).thenReturn(false);

        Executable executable = () -> authenticationService.refresh(username, refreshToken);
        var exception = assertThrows(NoSuchRefreshTokenException.class, executable);

        assertEquals("NO_SUCH_REFRESH_TOKEN_EXCEPTION", exception.getReason());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
    }

    @Test
    void testRefreshIfRefreshTokenExists() {
        String username = "username";
        String refreshToken = "ABC123";

        when(refreshTokenRepository.existsByUserLoginAndToken(username, refreshToken)).thenReturn(true);

        JwtTokenDto jwtTokenDto = authenticationService.refresh(username, refreshToken);

        JwtUtil jwtUtil = new JwtUtil(SecurityConfiguration.KEY);

        try {
            Claims claims = jwtUtil.verifyAndGetClaims(jwtTokenDto.getToken());

            assertEquals("username", claims.getSubject());
            assertApproximatelyEquals(
                    Instant.now().getEpochSecond() * 1000,
                    claims.getIssuedAt().getTime(),
                    1000);
            assertEquals("server-core", claims.getIssuer());

            Duration expiresIn = Duration.ofMinutes(10);
            assertApproximatelyEquals(
                    Instant.now().plus(expiresIn).getEpochSecond() * 1000,
                    claims.getExpiration().getTime(),
                    1000);

        } catch (Exception e) {
            fail(e);
        }

    }

    private void assertApproximatelyEquals(long expected, long actual, long maxDifference) {
        assertTrue(Math.abs(expected - actual) < maxDifference);
    }

    private static record MockUserDetailsService(UserDetails userDetails) implements UserDetailsService {

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            if ("username".equals(username)) {
                return userDetails;
            }
            throw new UsernameNotFoundException("UsernameNotFoundException");
        }
    }
}