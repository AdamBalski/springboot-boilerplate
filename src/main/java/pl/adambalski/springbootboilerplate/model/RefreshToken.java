package pl.adambalski.springbootboilerplate.model;

import pl.adambalski.springbootboilerplate.security.SecurityConfiguration;
import pl.adambalski.springbootboilerplate.util.RandomAlphaNumericStringGenerator;

import javax.persistence.*;
import javax.servlet.http.Cookie;
import java.sql.Date;
import java.time.Instant;
import java.time.Period;
import java.util.Objects;

/**
 * Is used for authenticating users that want to get an access token,
 * which you can use to authenticate to other endpoints.<br><br>
 *
 * @author Adam Balski
 * @see pl.adambalski.springbootboilerplate.repository.RefreshTokenRepository
 * @see Cookie
 * @see User
 */
@Entity
@Table(name = "refresh_token", schema = "public")
public final class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false, columnDefinition = "", unique = true, updatable = false)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", columnDefinition = "uuid", updatable = false, nullable = false)
    private User user;

    @Column(name = "token", columnDefinition = "varchar(12)", updatable = false, nullable = false)
    private String token;

    @Column(name = "expiration_date", columnDefinition = "date", updatable = false, nullable = false)
    private java.sql.Date expirationDate;

    public RefreshToken() {

    }

    public RefreshToken(long id, User user, String token, Date expirationDate) {
        this.id = id;
        this.user = user;
        this.token = token;
        this.expirationDate = expirationDate;
    }

    public static RefreshToken createRefreshToken(User user, RandomAlphaNumericStringGenerator randomAlphaNumericStringGenerator) {
        String token = randomAlphaNumericStringGenerator.generate();

        Instant now = Instant.now();
        Period expirationPeriod = SecurityConfiguration.REFRESH_TOKEN_EXPIRATION_PERIOD;
        java.sql.Date expirationDate = new Date(now.plus(expirationPeriod).toEpochMilli());

        return new RefreshToken(0, user, token, expirationDate);
    }

    public Cookie toCookie() {
        Cookie cookie = new Cookie("refresh_token", this.getToken());

        int secondsToExpirationDate = (int)(expirationDate.getTime() - Instant.now().toEpochMilli()) / 1000;
        cookie.setMaxAge(secondsToExpirationDate);

        cookie.setHttpOnly(true);
        // "/api/auth/authenticate" and "/api/auth/get-access-token"
        cookie.setPath("/api/auth/");
        cookie.setComment("The refresh token is used to make sure the client is log on, when getting the access token.");

        return cookie;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date date) {
        this.expirationDate = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefreshToken that = (RefreshToken) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(user, that.user) &&
                Objects.equals(token, that.token) &&
                Objects.equals(expirationDate, that.expirationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, token, expirationDate);
    }

    @Override
    public String toString() {
        //noinspection StringBufferReplaceableByString
        return new StringBuilder().append("RefreshToken{")
                .append("id=")
                    .append(id)
                .append(", user=")
                    .append(user)
                .append(", token='")
                    .append(token).append('\'')
                .append(", expirationDate=")
                    .append(expirationDate)
                .append('}').toString();
    }
}
