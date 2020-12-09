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
 */
@Service
public class AdminService {
    private AdminRepository adminRepository;

    @Autowired
    private void setUserRepository(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public User getUserDataByUUID(UUID uuid) {
        Optional<User> optionalUser = adminRepository.getUserDataByUUID(uuid);
        return optionalUser.orElseThrow(NoSuchUserException::new);
    }

    public User getUserDataByLogin(String login) {
        Optional<User> optionalUser = adminRepository.getUserDataByLogin(login);
        return optionalUser.orElseThrow(NoSuchUserException::new);
    }

    public void deleteUserByLogin(String login) {
        if(!adminRepository.deleteUserByLogin(login)) {
            throw new NoSuchUserException();
        }
    }
}
