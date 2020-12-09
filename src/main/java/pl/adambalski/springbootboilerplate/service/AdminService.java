package pl.adambalski.springbootboilerplate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.adambalski.springbootboilerplate.exception.NoSuchUserException;
import pl.adambalski.springbootboilerplate.model.User;
import pl.adambalski.springbootboilerplate.repository.AdminRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Performs main logic of operations like getting data from a user account or deleting a user account.<br><br>
 *
 * @author Adam Balski
 * @see AdminRepository
 * @see pl.adambalski.springbootboilerplate.controller.admin.AdminController
 */
@Service
public class AdminService {
    private final AdminRepository adminRepository;

    @Autowired
    AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public User getUserByUUID(UUID uuid) throws NoSuchUserException {
        Optional<User> optionalUser = adminRepository.getUserByUUID(uuid);
        return optionalUser.orElseThrow(NoSuchUserException::new);
    }

    public User getUserByLogin(String login) throws NoSuchUserException {
        Optional<User> optionalUser = adminRepository.getUserByLogin(login);
        return optionalUser.orElseThrow(NoSuchUserException::new);
    }

    public void deleteUserByLogin(String login) throws NoSuchUserException {
        if(!adminRepository.deleteUserByLogin(login)) {
            throw new NoSuchUserException();
        }
    }
}
