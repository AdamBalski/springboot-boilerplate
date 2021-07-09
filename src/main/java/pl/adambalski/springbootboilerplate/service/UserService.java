package pl.adambalski.springbootboilerplate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.adambalski.springbootboilerplate.dto.SignUpUserDto;
import pl.adambalski.springbootboilerplate.exception.AtLeastOneFieldIncorrectException;
import pl.adambalski.springbootboilerplate.exception.EmailIsTakenException;
import pl.adambalski.springbootboilerplate.exception.LoginIsTakenException;
import pl.adambalski.springbootboilerplate.exception.NoSuchUserException;
import pl.adambalski.springbootboilerplate.model.User;
import pl.adambalski.springbootboilerplate.repository.UserRepository;
import pl.adambalski.springbootboilerplate.validation.SignUpUserDtoValidationResult;
import pl.adambalski.springbootboilerplate.validation.SignUpUserDtoValidator;

import java.util.UUID;

/**
 * Performs main logic of operations like registering, authentication and deletion of an account.<br><br>
 *
 * @author Adam Balski
 * @see UserRepository
 * @see pl.adambalski.springbootboilerplate.controller.user.UserController
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean deleteUserByLogin(String login) {
        return userRepository.deleteByLogin(login) == 1;
    }

    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(NoSuchUserException::new);
    }

    public User getUserByUUID(UUID uuid) {
        return userRepository.findById(uuid).orElseThrow(NoSuchUserException::new);
    }

    public void addSignUpUserDto(SignUpUserDto dto) {
        // Throws HTTP400 if not valid
        checkIfValid(dto);
        // Throws HTTP400 if a field is taken
        checkIfTaken(dto);

        // Add
        User user = User.valueOf(dto, passwordEncoder);
        addUser(user);
    }

    // Throws HTTP400 (with a reason) if not valid
    private void checkIfValid(SignUpUserDto dto) {
        SignUpUserDtoValidationResult validationResult = SignUpUserDtoValidator.validate(dto);
        if(!validationResult.isSuccess()) {
            throw new AtLeastOneFieldIncorrectException();
        }
    }

    // Throws HTTP400 (with a field taken) if taken
    private void checkIfTaken(SignUpUserDto dto) {
        if(userRepository.existsByLoginOrEmail(dto.login(), dto.email())) {
            if(userRepository.existsByLogin(dto.login())) {
                throw new LoginIsTakenException();
            }
            else {
                throw new EmailIsTakenException();
            }
        }
    }

    private void addUser(User user) {
        userRepository.save(user);
    }
}
