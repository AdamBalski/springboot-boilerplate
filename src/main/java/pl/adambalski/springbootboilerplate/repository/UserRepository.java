package pl.adambalski.springbootboilerplate.repository;

import pl.adambalski.springbootboilerplate.model.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Interface containing functions used to communicate with data source, for example database.<br><br>
 *
 * @author Adam Balski
 */
@SuppressWarnings("SameReturnValue")
public interface UserRepository {
    // Get by
    Optional<User> getByUUID(UUID uuid);
    Optional<User> getByLogin(String login);

    // Exists by
    boolean existsByLoginOrEmail(String login, String email);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);

    // Add, delete
    boolean addUser(User user);
    boolean deleteUser(String login);

    // Ban, unban
    boolean banEmail(String email);
    boolean unbanEmail(String email);
}
