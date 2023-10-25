package pl.adambalski.springbootboilerplate.repository;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.adambalski.springbootboilerplate.dto.SignUpUserDto;
import pl.adambalski.springbootboilerplate.model.RefreshToken;
import pl.adambalski.springbootboilerplate.model.User;
import pl.adambalski.springbootboilerplate.security.PasswordEncoderFactory;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RefreshTokenRepositoryTest {
    @Rule
    PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:12.7");

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    private List<User> users;
    private List<RefreshToken> expired;
    private List<RefreshToken> nonExpired;
    private List<RefreshToken> all;

    @BeforeEach
    void init() {
        users = prepareUsers(createSignUpUserDtoList());

        expired = prepareExpired();
        nonExpired = prepareNonExpired(getNow());
        all = prepareAll();
    }

    private List<SignUpUserDto> createSignUpUserDtoList() {
        return List.of(
                new SignUpUserDto("username1", "fullname1", "e1@mail.com", "password1", "password1"),
                new SignUpUserDto("username2", "fullname2", "e2@mail.com", "password2", "password2"),
                new SignUpUserDto("username3", "fullname3", "e3@mail.com", "password3", "password3")
        );
    }

    private List<User> prepareUsers(List<SignUpUserDto> signUpUserDtoList) {
        Function<SignUpUserDto, User> signUpUserDtoToUserConverter = signUpUserDto -> {
            PasswordEncoder passwordEncoder = new PasswordEncoderFactory().passwordEncoderBean();
            return User.valueOf(signUpUserDto, passwordEncoder);
        };

        return signUpUserDtoList.stream().map(signUpUserDtoToUserConverter).toList();
    }

    private List<RefreshToken> prepareExpired() {
        return List.of(
                new RefreshToken(0, users.get(0).getLogin(), "120ABC", new Date(0)),
                new RefreshToken(0, users.get(1).getLogin(), "DE1ABC", new Date(100)),
                new RefreshToken(0, users.get(2).getLogin(), "ABC321", new Date(2137))
        );
    }

    private List<RefreshToken> prepareNonExpired(long now) {
        return List.of(
                new RefreshToken(0, users.get(1).getLogin(), "A123EF", new Date(now + 10)),
                new RefreshToken(0, users.get(0).getLogin(), "123DEF", new Date(now + 100)),
                new RefreshToken(0, users.get(2).getLogin(), "ABC123", new Date(now + 2137))
        );
    }

    private List<RefreshToken> prepareAll() {
        List<RefreshToken> all = new ArrayList<>(expired.size() + nonExpired.size());
        all.addAll(expired);
        all.addAll(nonExpired);

        return all;
    }

    private long getNow() {
        return Instant.now().toEpochMilli();
    }

    void checkIfDbContainsTheSameElementsAsList(List<RefreshToken> list) {
        Set<RefreshToken> refreshTokensFoundInDb = refreshTokenRepository
                .findAll()
                .stream()
                // id is set to 0, because all ids in the list are 0
                .peek(refreshToken -> refreshToken.setId(0l))
                .collect(Collectors.toSet());

        assertEquals(new HashSet<>(list), refreshTokensFoundInDb);
    }

    @Test
    void testDeleteALlExpiredWhenAllRefreshTokensAreExpired() {
        userRepository.saveAll(users);
        refreshTokenRepository.saveAll(expired);

        refreshTokenRepository.deleteAllByExpirationDateBeforeNow();

        checkIfDbContainsTheSameElementsAsList(List.of());
    }

    @Test
    void testDeleteALlExpiredWhenSomeRefreshTokensAreExpired() {
        userRepository.saveAll(users);
        refreshTokenRepository.saveAllAndFlush(all);

        refreshTokenRepository.deleteAllByExpirationDateBeforeNow();

        checkIfDbContainsTheSameElementsAsList(nonExpired);
    }

    @Test
    void testDeleteAllExpiredWhenNoneRefreshTokensAreExpired() {
        userRepository.saveAll(users);
        refreshTokenRepository.saveAll(nonExpired);

        refreshTokenRepository.deleteAllByExpirationDateBeforeNow();

        checkIfDbContainsTheSameElementsAsList(nonExpired);
    }

    @Test
    void testDeleteALlExpiredWhenThereIsNoRefreshTokens() {
        userRepository.saveAll(users);

        refreshTokenRepository.deleteAllByExpirationDateBeforeNow();

        checkIfDbContainsTheSameElementsAsList(List.of());
    }

    @Test
    void testExistsByUserAndTokenWhenThereIsNoneRefreshTokens() {
        assertFalse(refreshTokenRepository.existsByUserLoginAndToken(users.get(0).getLogin(), "TOKEN"));
    }

    @Test
    void testExistsByUserAndTokenWhenThereIsUser() {
        User user = users.get(0);
        RefreshToken refreshToken = new RefreshToken(0, user.getLogin(), "TOKEN1", new Date(getNow() + 2137));

        userRepository.save(user);
        refreshTokenRepository.save(refreshToken);

        boolean result = refreshTokenRepository.existsByUserLoginAndToken(user.getLogin(), "TOKEN2");

        assertFalse(result);
    }

    @Test
    void testExistsByUserAndTokenWhenThereIsToken() {
        User user = users.get(0);
        User anotherUser = users.get(1);

        RefreshToken refreshToken = new RefreshToken(0, user.getLogin(), "TOKEN1", new Date(getNow() + 2137));

        userRepository.save(user);
        userRepository.save(anotherUser);
        refreshTokenRepository.save(refreshToken);

        boolean result = refreshTokenRepository.existsByUserLoginAndToken(anotherUser.getLogin(), "TOKEN1");

        assertFalse(result);
    }

    @Test
    void testExistsByUserAndTokenWhenThereIsUserAndTokenButInDifferentRows() {
        User user = users.get(0);
        User anotherUser = users.get(1);

        RefreshToken refreshToken = new RefreshToken(0, user.getLogin(), "TOKEN1", new Date(getNow() + 2137));
        RefreshToken anotherRefreshToken = new RefreshToken(0, anotherUser.getLogin(), "TOKEN2", new Date(getNow() + 2137));

        userRepository.save(user);
        userRepository.save(anotherUser);

        refreshTokenRepository.save(refreshToken);
        refreshTokenRepository.save(anotherRefreshToken);

        boolean result = refreshTokenRepository.existsByUserLoginAndToken(user.getLogin(), "TOKEN2");

        assertFalse(result);
    }

    @Test
    void testExistsByUserAndTokenWhenThereIsUserAndTokenInTheSameRow() {
        User user = users.get(0);
        RefreshToken refreshToken = new RefreshToken(0, user.getLogin(), "TOKEN1", new Date(getNow() + 2137));

        userRepository.save(user);
        refreshTokenRepository.save(refreshToken);

        boolean result = refreshTokenRepository.existsByUserLoginAndToken(user.getLogin(), "TOKEN1");

        assertTrue(result);
    }
}