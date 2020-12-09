package pl.adambalski.springbootboilerplate.service;

import org.springframework.beans.factory.annotation.Autowired;
import pl.adambalski.springbootboilerplate.exception.NoSuchUserException;
import pl.adambalski.springbootboilerplate.model.User;
import pl.adambalski.springbootboilerplate.repository.AdminRepository;

import java.util.Optional;
import java.util.UUID;

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

    boolean deleteUserByLogin(String login) {
        return adminRepository.deleteUserByLogin(login);
    }
}
