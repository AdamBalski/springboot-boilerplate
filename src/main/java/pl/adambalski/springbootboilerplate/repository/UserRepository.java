package pl.adambalski.springbootboilerplate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.adambalski.springbootboilerplate.model.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Mostly used in {@link pl.adambalski.springbootboilerplate.service.UserService}.<br><br>
 *
 * @author Adam Balski
 * @see pl.adambalski.springbootboilerplate.service.UserService
 */
@SuppressWarnings("SameReturnValue")
@Transactional(readOnly = true)
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    // Get by
    Optional<User> findByLogin(String login);

    // Exists by
    boolean existsByLoginOrEmail(String login, String email);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);

    // Delete
    @Modifying
    int deleteByLogin(String login);
}
