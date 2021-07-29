package pl.adambalski.springbootboilerplate.repository;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.adambalski.springbootboilerplate.dto.SignUpUserDto;
import pl.adambalski.springbootboilerplate.model.User;
import pl.adambalski.springbootboilerplate.security.PasswordEncoderFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ComponentScan(basePackages = {
        "pl.adambalski.springbootboilerplate.repository"
},
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = {
                                UserRepository.class
                        }
                )
        }
)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Rule
    PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:12.7");
    
    @Autowired
    UserRepository userRepository;

    PasswordEncoder passwordEncoder = new PasswordEncoderFactory().passwordEncoderBean();

    User user;

    @BeforeEach
    void init() {
        user = createUser();
    }

    @Test
    void testGetUserByUUIDWhenUserDoesExist() {
        userRepository.save(user);

        Optional<User> userOptional = userRepository.findById(user.getUuid());

        assertTrue(userOptional.isPresent());
        assertEquals(userOptional.get(), user);
    }

    @Test
    void testGetUserByUUIDWhenUserDoesNotExist() {
        assertTrue(userRepository.findById(UUID.randomUUID()).isEmpty());
    }

    @Test
    void testGetUserByLoginWhenUserDoesExist() {
        userRepository.save(user);

        Optional<User> userOptional = userRepository.findByLogin(user.getLogin());

        assertTrue(userOptional.isPresent());
        assertEquals(userOptional.get(), user);
    }

    @Test
    void testGetUserByLoginWhenUserDoesNotExist() {
        assertTrue(userRepository.findByLogin("login").isEmpty());
    }

    @Test
    void testExistsByLoginOrEmailWhenUserHasBothLoginAndEmail() {
        SignUpUserDto signUpUserDto = new SignUpUserDto(
                "login", "Log In", "email@email.com", "p", "p");
        User user = User.valueOf(signUpUserDto, passwordEncoder);

        userRepository.save(user);

        assertTrue(userRepository.existsByLoginOrEmail(
                user.getLogin(),
                user.getEmail()
        ));
    }

    @Test
    void testExistsByLoginOrEmailWhenOneUserHasLoginMatchingAndOtherHasEmailMatching() {
        SignUpUserDto signUpUserDto1 = new SignUpUserDto(
                "login1", "Log In", "email1@email.com", "p", "p");
        User user1 = User.valueOf(signUpUserDto1, passwordEncoder);

        SignUpUserDto signUpUserDto2 = new SignUpUserDto(
                "login2", "Log In", "email2@email.com", "p", "p");
        User user2 = User.valueOf(signUpUserDto2, passwordEncoder);

        userRepository.saveAll(List.of(user1, user2));

        assert !user1.getLogin().equals(user2.getLogin());
        assert !user2.getEmail().equals(user1.getEmail());

        assertTrue(userRepository.existsByLoginOrEmail(user1.getLogin(), user2.getEmail()));
        assertTrue(userRepository.existsByLoginOrEmail(user2.getLogin(), user1.getEmail()));
    }

    @Test
    void testExistsByLoginOrEmailWhenUserHasLogin() {
        SignUpUserDto signUpUserDto = new SignUpUserDto(
                "login", "Log In", "email@email.com", "p", "p");
        User user = User.valueOf(signUpUserDto, passwordEncoder);

        userRepository.save(user);

        String userEmail = user.getEmail();
        String differentEmail = "different@email.com";
        assert !userEmail.equals(differentEmail);

        assertTrue(userRepository.existsByLoginOrEmail(
                user.getLogin(),
                differentEmail
        ));
    }

    @Test
    void testExistsByLoginOrEmailWhenUserHasEmail() {
        SignUpUserDto signUpUserDto = new SignUpUserDto(
                "login", "Log In", "email@email.com", "p", "p");
        User user = User.valueOf(signUpUserDto, passwordEncoder);

        userRepository.save(user);

        String userLogin = user.getLogin();
        String differentLogin = "differentLogin";
        assert !userLogin.equals(differentLogin);

        assertTrue(userRepository.existsByLoginOrEmail(
                differentLogin,
                user.getEmail()
        ));

    }

    @Test
    void testExistsByLoginOrEmailWhenUserDoesNotExist() {
        assertFalse(userRepository.existsByLoginOrEmail("login", "email"));
    }

    @Test
    void testExistsByLoginWhenUserDoesExist() {
        userRepository.save(user);

        assertTrue(userRepository.existsByLogin(user.getLogin()));
    }

    @Test
    void testExistsByLoginWhenUserDoesNotExist() {
        assertFalse(userRepository.existsByLogin("login"));
    }

    @Test
    void testExistsByEmailWhenUserDoesExist() {
        userRepository.save(user);

        assertTrue(userRepository.existsByEmail(user.getEmail()));
    }

    @Test
    void testExistsByEmailWhenUserDoesNotExist() {
        assertFalse(userRepository.existsByEmail("log@in.png"));
    }

    @Test
    void testDeleteUserByLoginWhenUserDoesExist() {
        userRepository.save(user);

        assert userRepository.existsById(user.getUuid());

        assertEquals(1, userRepository.deleteByLogin(user.getLogin()));
        assertFalse(userRepository.existsById(user.getUuid()));
    }

    @Test
    void testDeleteUserByLoginWhenUserDoesNotExist() {
        assertEquals(0, userRepository.deleteByLogin("login"));
    }

    private User createUser() {
        SignUpUserDto signUpUserDto = new SignUpUserDto("login", "Log In", "log@in.png", "password", "password");
        return User.valueOf(signUpUserDto, passwordEncoder);
    }
}
