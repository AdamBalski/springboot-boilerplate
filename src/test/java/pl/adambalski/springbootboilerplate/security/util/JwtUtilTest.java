package pl.adambalski.springbootboilerplate.security.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import pl.adambalski.springbootboilerplate.security.SecurityConfiguration;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {
    private JwtUtil jwtUtil;
    private JwtParser jwtParser;

    @BeforeEach
    void init() {
        SecretKey secretKey = Keys.hmacShaKeyFor(
                new byte[]{
                        -109, -72, -17, -18, 9, 108, -33, -126,
                        111, -38, 10, 65, 31, -4, 124, -117, 96,
                        -4, -127, 119, -76, -121, 15, -48, 15, 52,
                        -35, 103, -76, 28, 10, 122, -83, 78, 49
                        , 95, -2, -31, 56, -9, 98, -40, -48, 13,
                        -13, -118, 99, 19, -50, -9, 127, -83, 88,
                        101, 70, -79, 106, 11, 24, -82, 62, -2, -14, 126});
        this.jwtUtil = new JwtUtil(secretKey);
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build();
    }

    @Test
    void testVerifyAndGetClaimsThrowsSignatureExceptionWithInvalidSignature() {
        //e4a98f78-a251-478f-bd25-d51c4d1980aa
        // this token is not signed with this.secretKey
        //noinspection SpellCheckingInspection
        String token =
                "Bearer eyJhbGciOiJIUzUxMiJ9." +
                "eyJzdWIiOiJlNGE5OGY3OC1hMjUxLTQ3OGYtYmQyNS1kNTFjNGQxOTgwYWEiLCJpYXQiOjE1OTk0MjUyMjcsImV4cCI6MTU5OTQyNTgyNywiaXNzIjoic2VydmVyLWNvcmUifQ." +
                "8ll_1tODUBBYU-0Nkao4iC6juaE4elBXu-FGviT7dFiWmbBqtuPBeBdkxlHOC5JA5vQXocGEw2b0Y7S4FcLdXA";

        Executable executable = () -> jwtUtil.verifyAndGetClaims(token);
        assertThrows(SignatureException.class, executable);
    }

    @Test
    void testVerifyAndGetClaimsDoesNotThrowWithValidSignature() {
        String token = jwtUtil.tokenOf(UUID.randomUUID());

        Executable executable = () -> jwtUtil.verifyAndGetClaims(token);
        assertDoesNotThrow(executable);
    }

    @Test
    void testSubjectFromVerifyAndGetClaims() {
        UUID subject = UUID.randomUUID();
        String token = jwtUtil.tokenOf(subject);

        String actual = jwtUtil.verifyAndGetClaims(token).getSubject();
        assertEquals(subject.toString(), actual);
    }

    @Test
    void testIssuerFromVerifyAndGetClaims() {
        String token = jwtUtil.tokenOf(UUID.randomUUID());

        String actual = jwtUtil.verifyAndGetClaims(token).getIssuer();
        assertEquals("server-core", actual);
    }

    @Test
    void testIssuedAtFromVerifyAndGetClaims() {
        String token = jwtUtil.tokenOf(UUID.randomUUID());

        int computationTimeMargin = 100;
        Date expected = new Date();
        Date actual = jwtUtil.verifyAndGetClaims(token).getIssuedAt();

        assertTrue(
                Math.abs(expected.compareTo(actual)) < computationTimeMargin
        );
    }

    @Test
    void testExpirationDateFromVerifyAndGetClaims() {
        String token = jwtUtil.tokenOf(UUID.randomUUID());

        int computationTimeMargin = 100;
        Date expected = Date.from(Instant.now().plus(Duration.ofMinutes(10)));
        Date actual = jwtUtil.verifyAndGetClaims(token).getExpiration();

        assertTrue(
                Math.abs(expected.compareTo(actual)) < computationTimeMargin
        );
    }

    @Test
    void testIfSignatureIsValid() {
        UUID uuid = UUID.randomUUID();
        // without 'Bearer ' prefix
        String token = jwtUtil.tokenOf(uuid).replaceFirst(SecurityConfiguration.JWT_TOKEN_PREFIX, "");

        try {
            jwtParser.parseClaimsJws(token);
        } catch (SignatureException e) {
            fail(e);
        }
    }

    @Test
    void testSubjectOfTokenOf() {
        UUID uuid = UUID.randomUUID();
        // without 'Bearer ' prefix
        String actual = jwtUtil.tokenOf(uuid).replaceFirst(SecurityConfiguration.JWT_TOKEN_PREFIX, "");
        Claims claims = jwtParser.parseClaimsJws(actual).getBody();

        assertEquals(uuid.toString(), claims.getSubject());
    }

    @Test
    void testIssuerOfTokenOf() {
        UUID uuid = UUID.randomUUID();
        // without 'Bearer ' prefix
        String actual = jwtUtil.tokenOf(uuid).replaceFirst(SecurityConfiguration.JWT_TOKEN_PREFIX, "");
        Claims claims = jwtParser.parseClaimsJws(actual).getBody();

        assertEquals("server-core", claims.getIssuer());
    }

    @Test
    void testIfIssuedNow() {
        UUID uuid = UUID.randomUUID();
        // without 'Bearer ' prefix
        String actual = jwtUtil.tokenOf(uuid).replaceFirst(SecurityConfiguration.JWT_TOKEN_PREFIX, "");
        Claims claims = jwtParser.parseClaimsJws(actual).getBody();

        int computationTimeMargin = 10;

        assertTrue(claims.getIssuedAt()
                .compareTo(new Date()) < computationTimeMargin);
    }

    // should expire in 10 minutes
    @Test
    void testIfExpiresInProperExpirationTime() {
        UUID uuid = UUID.randomUUID();
        // without 'Bearer ' prefix
        String actual = jwtUtil.tokenOf(uuid).replaceFirst(SecurityConfiguration.JWT_TOKEN_PREFIX, "");
        Claims claims = jwtParser.parseClaimsJws(actual).getBody();

        int computationTimeMargin = 10;
        Date after10Minutes = Date.from(Instant.now().plus(Duration.ofMinutes(10)));

        assertTrue(claims.getExpiration()
                .compareTo(after10Minutes) < computationTimeMargin);
    }
}