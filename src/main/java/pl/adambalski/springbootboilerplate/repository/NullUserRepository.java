package pl.adambalski.springbootboilerplate.repository;

import org.springframework.stereotype.Repository;
import pl.adambalski.springbootboilerplate.model.User;

import java.util.Optional;
import java.util.UUID;

// Used for contextLoads() to pass and for app to start,
// before building JpaUserRepository
@Repository
public class NullUserRepository implements UserRepository {

    @Override
    public Optional<User> getUserByLogin(String login) {
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserByUUID(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public boolean existsByLoginOrEmail(String login, String email) {
        return false;
    }

    @Override
    public boolean existsByLogin(String login) {
        return false;
    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }

    @Override
    public boolean addUser(User user) {
        return false;
    }

    @Override
    public boolean deleteUserByLogin(String login) {
        return false;
    }
}
