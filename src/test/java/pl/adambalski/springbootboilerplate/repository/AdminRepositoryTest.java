package pl.adambalski.springbootboilerplate.repository;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.adambalski.springbootboilerplate.dto.SignUpUserDto;
import pl.adambalski.springbootboilerplate.model.User;
import pl.adambalski.springbootboilerplate.security.PasswordEncoderFactory;

import java.util.Optional;

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
                                UserRepository.class,
                        }
                )
        }
)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AdminRepositoryTest {
    @Container
    @Rule
    public PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:12.7");

    @Autowired
    AdminRepository adminRepository;

    PasswordEncoder passwordEncoder = new PasswordEncoderFactory().passwordEncoderBean();

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
