package pl.adambalski.springbootboilerplate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.adambalski.springbootboilerplate.dto.SignUpUserDto;
import pl.adambalski.springbootboilerplate.security.PasswordEncoderFactory;
import pl.adambalski.springbootboilerplate.security.SecurityConfiguration;
import pl.adambalski.springbootboilerplate.util.RandomAlphaNumericStringGenerator;

import javax.servlet.http.Cookie;
import java.sql.Date;
import java.time.Instant;
import java.time.Period;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class RefreshTokenTest {
    User user1;
    User user2;

    @BeforeEach
    void init() {
        PasswordEncoder passwordEncoder = new PasswordEncoderFactory().passwordEncoderBean();

        user1 = User.valueOf(
                new SignUpUserDto(
                        "login",
                        "fullName",
                        "e@mail.com",
                        "password1",
                        "password1" ),
                passwordEncoder
        );
        user2 = User.valueOf(
                new SignUpUserDto(
                        "login2",
                        "fullName2",
                        "e2@mail.com",
                        "password2",
                        "password2" ),
                passwordEncoder
        );
    }

    @Test
    void testEqualsIsSymmetricalForNonEqualRefreshTokens() {
        RefreshToken refreshToken1 = new RefreshToken(1, "user1", "123456", new Date(123456));
        RefreshToken refreshToken2 = new RefreshToken(2, "user2", "ABCDEF", new Date(123456));

        assertNotEquals(refreshToken1, refreshToken2);
        assertNotEquals(refreshToken2, refreshToken1);
    }

    @Test
    void testEqualsIsSymmetricalForEqualRefreshTokens() {
        RefreshToken refreshToken1 = new RefreshToken(1, "user1", "123456", new Date(123456));
        RefreshToken refreshToken2 = new RefreshToken(1, "user1", "123456", new Date(123456));


        assertEquals(refreshToken1, refreshToken2);
        assertEquals(refreshToken2, refreshToken1);
    }

    @Test
    void testEqualsIsTrueForEqualRefreshTokens() {
        RefreshToken refreshToken1 = new RefreshToken(1, "user1", "123456", new Date(123456));
        RefreshToken refreshToken2 = new RefreshToken(1, "user1", "123456", new Date(123456));

        assertEquals(refreshToken1, refreshToken2);
    }

    @Test
    void testEqualsIsFalseForNonEqualRefreshTokens() {
        RefreshToken refreshToken1 = new RefreshToken(1, "user1", "123456", new Date(123456));
        RefreshToken refreshToken2 = new RefreshToken(2, "user2", "654321", new Date(123456));

        assertNotEquals(refreshToken1, refreshToken2);
    }

    @Test
    void testEqualsIsTrueForTheSameRefreshToken() {
        RefreshToken refreshToken = new RefreshToken(1, "user1", "123456", new Date(123456));

        assertEquals(refreshToken, refreshToken);
    }


    @Test
    void testHashcodesAreTheSameForEqualRefreshTokens() {
        RefreshToken refreshToken1 = new RefreshToken(1, "user1", "123456", new Date(123456));
        RefreshToken refreshToken2 = new RefreshToken(1, "user1", "123456", new Date(123456));

        assertEquals(refreshToken1.hashCode(), refreshToken2.hashCode());
    }

    // a = b ^ b = c => a = c
    @Test
    void testEqualsIsReflexive() {
        RefreshToken a = new RefreshToken(1, "user1", "123456", new Date(123456));
        RefreshToken b = new RefreshToken(1, "user1", "123456", new Date(123456));
        RefreshToken c = new RefreshToken(1, "user1", "123456", new Date(123456));

        assertEquals(a, b);
        assertEquals(b, c);
        assertEquals(a, c);
    }

    // a = b ^ b != c => a != c
    @Test
    void testEqualsIsReflexive2() {
        RefreshToken a = new RefreshToken(1, "user1", "123456", new Date(123456));
        RefreshToken b = new RefreshToken(1, "user1", "123456", new Date(123456));
        RefreshToken c = new RefreshToken(2, "user2", "123456", new Date(123456));

        assertEquals(a, b);
        assertNotEquals(b, c);
        assertNotEquals(a, c);
    }

    @Test
    void testEqualsIsConsistent() {
        RefreshToken refreshToken1 = new RefreshToken(1, "user1", "123456", new Date(123456));
        RefreshToken refreshToken2 = new RefreshToken(1, "user1", "123456", new Date(123456));

        assertEquals(refreshToken1, refreshToken2);
        assertEquals(refreshToken1, refreshToken2);
        assertEquals(refreshToken1, refreshToken2);
        assertEquals(refreshToken1, refreshToken2);
        assertEquals(refreshToken1, refreshToken2);
    }

    @Test
    void testHashcodeIsConsistent() {
        RefreshToken refreshToken = new RefreshToken(1, "user1", "123456", new Date(123456));

        assertEquals(1, IntStream
                .generate(refreshToken::hashCode)
                .limit(10)
                .distinct()
                .count());
    }

    @Test
    void testEqualsWithNotRefreshToken() {
        //noinspection AssertBetweenInconvertibleTypes
        assertNotEquals(new RefreshToken(), new ArrayIndexOutOfBoundsException());
    }

    @Test
    void testEqualsWithNull() {
        assertNotEquals(new RefreshToken(), null);
    }

    @Test
    void testEqualsIfIdsAreNotTheSame() {
        RefreshToken refreshToken1 = new RefreshToken(1, "user1", "123456", new Date(123456));
        RefreshToken refreshToken2 = new RefreshToken(2, "user1", "123456", new Date(123456));

        assertNotEquals(refreshToken1, refreshToken2);
    }

    @Test
    void testEqualsIfUsersAreNotTheSame() {
        RefreshToken refreshToken1 = new RefreshToken(1, "user1", "123456", new Date(123456));
        RefreshToken refreshToken2 = new RefreshToken(1, "user2", "123456", new Date(123456));

        assertNotEquals(refreshToken1, refreshToken2);
    }

    @Test
    void testEqualsIfTokensAreNotTheSame() {
        RefreshToken refreshToken1 = new RefreshToken(1, "user1", "123456", new Date(123456));
        RefreshToken refreshToken2 = new RefreshToken(1, "user1", "ABCDEF", new Date(123456));

        assertNotEquals(refreshToken1, refreshToken2);
    }

    @Test
    void testEqualsIfExpirationDatesAreNotTheSame() {
        RefreshToken refreshToken1 = new RefreshToken(1, "user1", "123456", new Date(654321));
        RefreshToken refreshToken2 = new RefreshToken(1, "user1", "123456", new Date(123456));

        assertNotEquals(refreshToken1, refreshToken2);
    }

    @Test
    void testGetId() {
        RefreshToken refreshToken = new RefreshToken(1, "user1", "123456", new Date(123456));

        assertEquals(1, refreshToken.getId());
    }

    @Test
    void testGetUserLogin() {
        RefreshToken refreshToken = new RefreshToken(1, "user1", "123456", new Date(123456));

        assertEquals("user1", refreshToken.getUserLogin());
    }

    @Test
    void testGetToken() {
        RefreshToken refreshToken = new RefreshToken(1, "user1", "123456", new Date(123456));

        assertEquals("123456", refreshToken.getToken());
    }

    @Test
    void testGetExpirationDate() {
        RefreshToken refreshToken = new RefreshToken(1, "user1", "123456", new Date(123456));

        assertEquals(new Date(123456), refreshToken.getExpirationDate());
    }

    @Test
    void testSetId() {
        RefreshToken refreshToken = new RefreshToken(1, "user1", "123456", new Date(123456));

        refreshToken.setId(2L);

        assertEquals(2, refreshToken.getId());
    }

    @Test
    void testSetUser() {
        RefreshToken refreshToken = new RefreshToken(1, "user1", "123456", new Date(123456));

        refreshToken.setUserLogin("user2");

        assertEquals("user2", refreshToken.getUserLogin());
    }

    @Test
    void testSetToken() {
        RefreshToken refreshToken = new RefreshToken(1, "user1", "123456", new Date(123456));

        refreshToken.setToken("654321");

        assertEquals("654321", refreshToken.getToken());
    }

    @Test
    void testSetExpirationDate() {
        RefreshToken refreshToken = new RefreshToken(1, "user1", "123456", new Date(123456));

        refreshToken.setExpirationDate(new Date(654321));

        assertEquals(new Date(654321), refreshToken.getExpirationDate());
    }

    @Test
    void testNoArgsConstructor() {
        RefreshToken refreshToken = new RefreshToken();

        assertNull(refreshToken.getId());
        assertNull(refreshToken.getUserLogin());
        assertNull(refreshToken.getToken());
        assertNull(refreshToken.getExpirationDate());
    }

    @Test
    void testAllArgsConstructor() {
        RefreshToken refreshToken = new RefreshToken(1, "user1", "123456", new Date(2137));

        assertEquals(1, refreshToken.getId());
        assertEquals("user1", refreshToken.getUserLogin());
        assertEquals("123456", refreshToken.getToken());
        assertEquals(new Date(2137), refreshToken.getExpirationDate());
    }

    @Test
    void testToString() {
        RefreshToken refreshToken = new RefreshToken(1, "user1", "123456", new Date(2137));

        String expected = "RefreshToken{id=1, userLogin='user1', token='123456', expirationDate=1970-01-01}";
        String actual = refreshToken.toString();

        assertEquals(expected, actual);
    }

    @Test
    void testCreateRefreshToken() {
        Period expirationPeriod = SecurityConfiguration.REFRESH_TOKEN_EXPIRATION_PERIOD;
        Date expirationDate = new Date(Instant.now().plus(expirationPeriod).toEpochMilli());

        int length = 12;
        RefreshToken refreshToken = RefreshToken.createRefreshToken("user1", new RandomAlphaNumericStringGenerator(length));

        assertEquals(0, refreshToken.getId());
        assertEquals("user1", refreshToken.getUserLogin());

        assertTrue(refreshToken.getToken().matches("^[A-Za-z0-9]{%s}$".formatted(length)));
        // java.sql.Date doesn't override Object::equals, so we have to first convert it to a string
        assertEquals(refreshToken.getExpirationDate().toString(), expirationDate.toString());
    }

    @Test
    void testToCookie() {
        RefreshToken refreshToken = new RefreshToken(1, "user1", "123456", new Date(Instant.now().toEpochMilli()));
        Cookie cookie = refreshToken.toCookie();

        assertTrue(cookie.isHttpOnly());
        assertFalse(cookie.getSecure());
        assertEquals("123456", cookie.getValue());
        assertEquals("refresh_token", cookie.getName());
        String expectedPurpose = "The refresh token is used to " +
                "make sure the client is log on, when getting the access token.";
        assertEquals(expectedPurpose, cookie.getComment());
        assertEquals(0, cookie.getMaxAge());
        assertNull(cookie.getDomain());
        assertEquals(0, cookie.getVersion());
        assertEquals("/api/auth/", cookie.getPath());
    }
}