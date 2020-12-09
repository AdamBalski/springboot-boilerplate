package pl.adambalski.springbootboilerplate.repository;

import org.springframework.stereotype.Repository;
import pl.adambalski.springbootboilerplate.model.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Mostly used in {@link pl.adambalski.springbootboilerplate.service.AdminService}.<br><br>
 *
 * @author Adam Balski
 * @see pl.adambalski.springbootboilerplate.service.AdminService
 */
@Repository
public interface AdminRepository {
    Optional<User> getUserByUUID(UUID uuid);
    Optional<User> getUserByLogin(String login);
    boolean deleteUserByLogin(String login);
}
