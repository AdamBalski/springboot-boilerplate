package pl.adambalski.springbootboilerplate.repository;

import pl.adambalski.springbootboilerplate.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface containing functions used to communicate with data source, for example database.<br><br>
 *
 * @author Adam Balski
 */
public interface UserRepository {
    Optional<User> getByLogin(String login);
    Optional<User> getByUUID(UUID uuid);
    boolean addUser(User user);
    boolean doesUserWithEmailOrLoginExist(String email, String login);
    boolean deleteUser(String login);
    boolean banEmail(String email);
    boolean unbanEmail(String email);
    List<User> get10UsersStartingFromNth(int n);
}
