package pl.adambalski.springbootboilerplate.security.util;

import io.jsonwebtoken.*;
import pl.adambalski.springbootboilerplate.security.SecurityConfiguration;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * SWT utility class used for generating bearer tokens and verifying them.<br>
 * {@link #tokenOf(String)} gets JWT out of the uuid and signs it with {@link SecretKey} object from constructor.
 * Token is in a form of 'Bearer ${SWT}'.<br>
 * {@link #verifyAndGetClaims(String)} gets {@link Claims}
 * and verifies if Jwt is malformed, expired or has an invalid signature.
 * Token is in a form of 'Bearer ${SWT}'<br><br>
 *
 * @see Jwts
 * @see Claims
 * @see UUID
 * @author Adam Balski
 */
public class JwtUtil {
    private final SecretKey key;
    private final Duration expiresIn;
    private final JwtParser jwtParser;

    public JwtUtil(SecretKey secretKey) {
        this(secretKey, Duration.ofMinutes(10));
    }

    private JwtUtil(SecretKey secretKey, Duration expiresIn) {
        this.key = secretKey;
        this.expiresIn = expiresIn;
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
    }

    // Checks for token malformation,
    // if the token got expired
    // and if signature is valid
    public Claims verifyAndGetClaims(String token) throws JwtException {
        if(token == null)   throw new JwtException("SWT token cannot be null");

        token = token.replaceFirst(SecurityConfiguration.JWT_TOKEN_PREFIX, "");
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public String tokenOf(String subject) {
        return SecurityConfiguration.JWT_TOKEN_PREFIX + Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(expiresIn)))
                .setIssuer("server-core")
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}
