package pl.adambalski.springbootboilerplate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.adambalski.springbootboilerplate.dto.SignUpUserDto;
import pl.adambalski.springbootboilerplate.model.User;
import pl.adambalski.springbootboilerplate.repository.UserRepository;
import pl.adambalski.springbootboilerplate.validation.SignUpUserDtoValidationResult;
import pl.adambalski.springbootboilerplate.validation.SignUpUserDtoValidator;

import java.util.Optional;
import java.util.UUID;

/**
 * Performs main logic of operations like registering, authentication and deletion of an account.<br><br>
 *
 * @author Adam Balski
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

    public boolean deleteUser(String login) {
        return userRepository.deleteUser(login);
    }

    public Optional<User> getUserByLogin(String login) {
        return userRepository.getByLogin(login);
    }

    public Optional<User> getUserByUUID(UUID uuid) {
        return userRepository.getByUUID(uuid);
    }

    public boolean addSignUpUserDto(SignUpUserDto dto) {
        // Throws HTTP400 if not valid
        checkIfValid(dto);
        // Throws HTTP400 if taken
        checkIfTaken(dto);

        // Add
        User user = User.valueOf(dto, passwordEncoder);
        return addUser(user);
    }

    // Throws HTTP400 (with a reason) if not valid
    private void checkIfValid(SignUpUserDto dto) {
        SignUpUserDtoValidationResult validationResult = SignUpUserDtoValidator.validate(dto);
        if(!validationResult.isSuccess()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SOME_FIELD_IS_INCORRECT");
        }
    }

    // Throws HTTP400 (with a field taken) if taken
    private void checkIfTaken(SignUpUserDto dto) {
        if(userRepository.existsByLoginOrEmail(dto.login(), dto.email())) {
            String reason;
            if(userRepository.existsByLogin(dto.login())) {
                reason = "LOGIN_IS_TAKEN";
            }
            else {
                reason = "EMAIL_IS_TAKEN";
            }

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason);
        }
    }

    public boolean addUser(User user) {
        return userRepository.addUser(user);
    }
}
