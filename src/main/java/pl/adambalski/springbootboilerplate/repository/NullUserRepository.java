package pl.adambalski.springbootboilerplate.repository;

import org.springframework.stereotype.Component;
import pl.adambalski.springbootboilerplate.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Used for contextLoads() to pass and for app to start,
// before building JpaUserRepository
@Component
public class NullUserRepository implements UserRepository {

    @Override
    public Optional<User> getByLogin(String login) {
        return Optional.empty();
    }

    @Override
    public Optional<User> getByUUID(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public boolean addUser(User user) {
        return false;
    }

    @Override
    public boolean doesUserWithEmailOrLoginExist(String email, String login) {
        return false;
    }

    @Override
    public boolean deleteUser(String login) {
        return false;
    }

    @Override
    public boolean banEmail(String email) {
        return false;
    }

    @Override
    public boolean unbanEmail(String email) {
        return false;
    }

    @Override
    public List<User> get10UsersStartingFromNth(int n) {
        return null;
    }
}
