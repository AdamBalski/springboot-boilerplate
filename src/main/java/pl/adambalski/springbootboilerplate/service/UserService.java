package pl.adambalski.springbootboilerplate.service;

import org.springframework.beans.factory.annotation.Autowired;
import pl.adambalski.springbootboilerplate.model.User;
import pl.adambalski.springbootboilerplate.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Performs main logic of operations such as registering, authentication and deletion of an account<br><br>
 *
 * @author Adam Balski
 */
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean deleteUserAndBanEmail(String login, String email) {
        return deleteUser(login) && userRepository.banEmail(email);
    }

    public boolean deleteUser(String login) {
        return userRepository.deleteUser(login);
    }


    public List<User> get10UsersStartingFromNth(int n) {
        return userRepository.get10UsersStartingFromNth(n);
    }

    public Optional<User> getUserByLogin(String login) {
        return userRepository.getByLogin(login);
    }

    public Optional<User> getUserByUUID(UUID uuid) {
        return userRepository.getByUUID(uuid);
    }

    public boolean unbanEmail(String email) {
        return userRepository.unbanEmail(email);
    }

    public boolean addUser(User user) {
        return userRepository.addUser(user);
    }

    public boolean doesUserWithEmailOrLoginExist(String email, String login) {
        return userRepository.doesUserWithEmailOrLoginExist(email, login);
    }
}
