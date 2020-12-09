package pl.adambalski.springbootboilerplate.repository;

import org.springframework.stereotype.Repository;
import pl.adambalski.springbootboilerplate.model.User;

import java.util.Optional;
import java.util.UUID;

// TODO link AdminService
/**
 * Mostly used in AdminService.<br><br>
 *
 * @author Adam Balski
 * @see //AdminService
 */
@Repository
public interface AdminRepository {
    Optional<User> getUserDataByUUID(UUID uuid);
    Optional<User> getUserDataByLogin(String login);
    boolean deleteUserByLogin(String login);
}
