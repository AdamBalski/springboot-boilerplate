package pl.adambalski.springbootboilerplate.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.adambalski.springbootboilerplate.dto.SignUpUserDto;
import pl.adambalski.springbootboilerplate.model.User;
import pl.adambalski.springbootboilerplate.security.PasswordEncoderFactory;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ComponentScan(basePackages = {
        "pl.adambalski.springbootboilerplate.repository",
        "pl.adambalski.springbootboilerplate.security"
},
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = {
                                PasswordEncoderFactory.class,
                                AdminJpaRepository.class
                        }
                )
        }
)
public class AdminJpaRepositoryTest {
    @Autowired
    AdminJpaRepository adminJpaRepository;

    PasswordEncoder passwordEncoder = new PasswordEncoderFactory().passwordEncoderBean();

    @AfterEach
    void destroy() {
        adminJpaRepository.findAll().forEach(System.out::println);
    }

    @Test
    void testFindByUUIDWhenUserExists() {
        SignUpUserDto signUpUserDto = new SignUpUserDto(
                "login",
                "Full Name",
                "a@a.jpg",
                "password",
                "password");
        User user = User.valueOf(signUpUserDto, passwordEncoder);
        UUID uuid = user.getUuid();
        adminJpaRepository.save(user);

        Optional<User> userOptional = adminJpaRepository.findByUuid(uuid);

        assertTrue(userOptional.isPresent());
        assertEquals(user, userOptional.get());
    }

    @Test
    void testFindByUUIDWhenUserDoesNotExist() {
        assertTrue(adminJpaRepository.findByUuid(UUID.randomUUID()).isEmpty());
    }

    @Test
    void testFindByLoginWhenUserExists() {
        String login = "login";
        SignUpUserDto signUpUserDto = new SignUpUserDto(
                login,
                "Full Name",
                "a@a.jpg",
                "password",
                "password");
        User user = User.valueOf(signUpUserDto, passwordEncoder);
        adminJpaRepository.save(user);

        Optional<User> userOptional = adminJpaRepository.findByLogin(login);

        assertTrue(userOptional.isPresent());
        assertEquals(user, userOptional.get());
    }

    @Test
    void testFindByLoginWhenUserDoesNotExist() {
        assertTrue(adminJpaRepository.findByLogin("login").isEmpty());
    }

    @Test
    void testBooleanDeleteUserByLoginWhenUserExists() {
        String login = "login";
        SignUpUserDto signUpUserDto = new SignUpUserDto(
                login,
                "Full Name",
                "a@a.jpg",
                "password",
                "password");
        User user = User.valueOf(signUpUserDto, passwordEncoder);
        adminJpaRepository.save(user);

        // assert that deletion was successful
        assertTrue(adminJpaRepository.deleteUserByLogin(login));
        // assert that user no longer exists
        assertFalse(adminJpaRepository.existsById(user.getUuid()));
    }

    @Test
    void testBooleanDeleteUserByLoginWhenUserDoesNotExist() {
        assertFalse(adminJpaRepository.deleteUserByLogin("login"));
    }

    @Test
    void testIntDeleteByLoginWhenUserExists() {
        String login = "login";
        SignUpUserDto signUpUserDto = new SignUpUserDto(
                login,
                "Full Name",
                "a@a.jpg",
                "password",
                "password");
        User user = User.valueOf(signUpUserDto, passwordEncoder);
        adminJpaRepository.save(user);

        // assert that deletion was successful
        assertEquals(1, adminJpaRepository.deleteByLogin(login));
        // assert that user no longer exists
        assertFalse(adminJpaRepository.existsById(user.getUuid()));
    }

    @Test
    void testIntDeleteByLoginWhenUserDoesNotExist() {
        assertEquals(0, adminJpaRepository.deleteByLogin("login"));
    }
}
