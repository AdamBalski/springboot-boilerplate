package pl.adambalski.springbootboilerplate.repository;

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
                                AdminRepository.class
                        }
                )
        }
)
public class AdminRepositoryTest {
    @Autowired
    AdminRepository adminRepository;

    PasswordEncoder passwordEncoder = new PasswordEncoderFactory().passwordEncoderBean();

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
        adminRepository.save(user);

        Optional<User> userOptional = adminRepository.findByUuid(uuid);

        assertTrue(userOptional.isPresent());
        assertEquals(user, userOptional.get());
    }

    @Test
    void testFindByUUIDWhenUserDoesNotExist() {
        assertTrue(adminRepository.findByUuid(UUID.randomUUID()).isEmpty());
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
        adminRepository.save(user);

        Optional<User> userOptional = adminRepository.findByLogin(login);

        assertTrue(userOptional.isPresent());
        assertEquals(user, userOptional.get());
    }

    @Test
    void testFindByLoginWhenUserDoesNotExist() {
        assertTrue(adminRepository.findByLogin("login").isEmpty());
    }

    @Test
    void testDeleteByLoginWhenUserExists() {
        String login = "login";
        SignUpUserDto signUpUserDto = new SignUpUserDto(
                login,
                "Full Name",
                "a@a.jpg",
                "password",
                "password");
        User user = User.valueOf(signUpUserDto, passwordEncoder);
        adminRepository.save(user);

        // assert that deletion was successful
        assertEquals(1, adminRepository.deleteByLogin(login));
        // assert that user no longer exists
        assertFalse(adminRepository.existsById(user.getUuid()));
    }

    @Test
    void testDeleteByLoginWhenUserDoesNotExist() {
        assertEquals(0, adminRepository.deleteByLogin("login"));
    }
}
