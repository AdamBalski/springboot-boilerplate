package pl.adambalski.springbootboilerplate.repository;

import org.springframework.stereotype.Repository;
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
@Repository
public interface UserRepository {
    // Get by
    Optional<User> getUserByUUID(UUID uuid);
    Optional<User> getUserByLogin(String login);

    // Exists by
    boolean existsByLoginOrEmail(String login, String email);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);

    // Add, delete
    boolean addUser(User user);
    boolean deleteUserByLogin(String login);
}
