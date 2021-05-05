package pl.adambalski.springbootboilerplate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.adambalski.springbootboilerplate.exception.NoSuchUserException;
import pl.adambalski.springbootboilerplate.model.User;
import pl.adambalski.springbootboilerplate.repository.AdminJpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Performs main logic of operations like getting data from a user account or deleting a user account.<br><br>
 *
 * @author Adam Balski
 * @see AdminJpaRepository
 * @see pl.adambalski.springbootboilerplate.controller.admin.AdminController
 * @see User
 */
@Service
public class AdminService {
    private final AdminJpaRepository adminJpaRepository;

    @Autowired
    AdminService(AdminJpaRepository adminJpaRepository) {
        this.adminJpaRepository = adminJpaRepository;
    }

    public User getUserByUUID(UUID uuid) throws NoSuchUserException {
        Optional<User> optionalUser = adminJpaRepository.findByUuid(uuid);
        return optionalUser.orElseThrow(NoSuchUserException::new);
    }

    public User getUserByLogin(String login) throws NoSuchUserException {
        Optional<User> optionalUser = adminJpaRepository.findByLogin(login);
        return optionalUser.orElseThrow(NoSuchUserException::new);
    }

    public void deleteUserByLogin(String login) throws NoSuchUserException {
        if(!adminJpaRepository.deleteUserByLogin(login)) {
            throw new NoSuchUserException();
        }
    }
}
