package pl.adambalski.springbootboilerplate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.adambalski.springbootboilerplate.model.RefreshToken;
import pl.adambalski.springbootboilerplate.model.User;

/**
 * Performs SQL queries on database. Primarily called by {@link pl.adambalski.springbootboilerplate.service.AuthenticationService}.<br><br>
 *
 * @author Adam Balski
 * @see pl.adambalski.springbootboilerplate.service.AuthenticationService
 * @see RefreshToken
 * @see User
 * @see pl.adambalski.springbootboilerplate.controller.user.AuthenticationController
 */
@Repository
@Transactional(readOnly = true)
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    @Modifying
    @Query(value =
            """
            delete from refresh_token
                where expiration_date < cast(NOW() AS DATE);
            """,
            nativeQuery = true
    )
    void deleteAllByExpirationDateBeforeNow();

    boolean existsByUserLoginAndToken(String userLogin, String token);
}
