package pl.adambalski.springbootboilerplate.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;
import pl.adambalski.springbootboilerplate.dto.SignUpUserDto;
import pl.adambalski.springbootboilerplate.exception.AtLeastOneFieldIncorrectException;
import pl.adambalski.springbootboilerplate.exception.EmailIsTakenException;
import pl.adambalski.springbootboilerplate.exception.LoginIsTakenException;
import pl.adambalski.springbootboilerplate.exception.NoSuchUserException;
import pl.adambalski.springbootboilerplate.model.Role;
import pl.adambalski.springbootboilerplate.model.User;
import pl.adambalski.springbootboilerplate.repository.UserRepository;
import pl.adambalski.springbootboilerplate.security.PasswordEncoderFactory;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    AutoCloseable autoCloseable;

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    // Mock that is injected in void init() method
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void init() {
        this.autoCloseable = MockitoAnnotations.openMocks(this);

        passwordEncoder = new PasswordEncoderFactory().passwordEncoderBean();
        ReflectionTestUtils.setField(userService, "passwordEncoder", passwordEncoder);
    }

    @AfterEach
    void destroy() throws Exception {
        this.autoCloseable.close();
    }

    @Test
    void testGetUserDataByLogin() {
        String login = "user";
        User user = getRandUser();
        Optional<User> userOptional = Optional.of(user);

        when(userRepository.getUserByLogin(login))
                .thenReturn(userOptional);

        assertEquals(user, userService.getUserByLogin(login));
    }


    @Test
    void testGetUserDataByLoginWhenThatLoginDoesNotExist() {
        String login = "user";
        when(userRepository.getUserByLogin(login))
                .thenReturn(Optional.empty());

        Executable executable = () -> userService.getUserByLogin(login);
        assertThrows(NoSuchUserException.class, executable);
    }

    @Test
    void testGetUserDataByUUID() {
        UUID uuid = UUID.randomUUID();
        User user = getRandUser();
        Optional<User> userOptional = Optional.of(user);

        when(userRepository.getUserByUUID(uuid))
                .thenReturn(userOptional);

        assertEquals(user, userService.getUserByUUID(uuid));
    }

    @Test
    void testGetUserDataByUUIDWhenThatUuidDoesNotExist() {
        UUID uuid = UUID.randomUUID();
        when(userRepository.getUserByUUID(uuid))
                .thenReturn(Optional.empty());

        Executable executable = () -> userService.getUserByUUID(uuid);
        assertThrows(NoSuchUserException.class, executable);
    }

    <T extends ResponseStatusException> void testAddSignUpUserDto(SignUpUserDto signUpUserDto,
                              boolean shouldThrowException,
                              Class<T> exceptionClassOughtToBeThrown) {
        Executable executable = () -> userService.addSignUpUserDto(signUpUserDto);
        if(shouldThrowException) {
            // assert that exception was thrown and get the exception
            var exception = assertThrows(ResponseStatusException.class, executable);
            assertEquals(exceptionClassOughtToBeThrown, exception.getClass());
            // verify nothing was put into repository
            Mockito.verify(userRepository, never()).addUser(any());
        }
        else {
            assertDoesNotThrow(executable);
            Mockito.verify(userRepository).addUser(any(User.class));
        }
    }

    @Test
    void testAddSignUpUserDtoWhenEverythingIsOk() {
        Mockito.when(userRepository.addUser(any(User.class))).thenReturn(true);
        SignUpUserDto signUpUserDto = new SignUpUserDto(
                "login",
                "Full Name",
                "e@mail.png",
                "password",
                "password");

        testAddSignUpUserDto(signUpUserDto, false, null);
    }

    @Test
    void testAddSignUpUserDtoWhenLoginIsInvalid() {
        SignUpUserDto signUpUserDto = new SignUpUserDto(
                "",
                "Full Name",
                "e@mail.png",
                "password",
                "password");

        testAddSignUpUserDto(signUpUserDto, true, AtLeastOneFieldIncorrectException.class);
    }

    @Test
    void testAddSignUpUserDtoWhenFullNameIsInvalid() {
        SignUpUserDto signUpUserDto = new SignUpUserDto(
                "login",
                "@",
                "e@mail.png",
                "password",
                "password");

        testAddSignUpUserDto(signUpUserDto, true, AtLeastOneFieldIncorrectException.class);
    }

    @Test
    void testAddSignUpUserDtoWhenLoginAndFullNameAreInvalid() {
        SignUpUserDto signUpUserDto = new SignUpUserDto(
                "",
                "@",
                "e@mail.png",
                "password",
                "password");

        testAddSignUpUserDto(signUpUserDto, true, AtLeastOneFieldIncorrectException.class);
    }

    @Test
    void testAddSignUpUserDtoWhenLoginIsTaken() {
        Mockito.when(userRepository.existsByLogin("login")).thenReturn(true);
        Mockito.when(userRepository.existsByLoginOrEmail("login", "e@mail.png")).thenReturn(true);

        SignUpUserDto signUpUserDto = new SignUpUserDto(
                "login",
                "Full Name",
                "e@mail.png",
                "password",
                "password");

        testAddSignUpUserDto(signUpUserDto, true, LoginIsTakenException.class);    }

    @Test
    void testAddSignUpUserDtoWhenEmailIsTaken() {
        Mockito.when(userRepository.existsByEmail("e@mail.com")).thenReturn(true);
        Mockito.when(userRepository.existsByLoginOrEmail("login", "e@mail.png")).thenReturn(true);

        SignUpUserDto signUpUserDto = new SignUpUserDto(
                "login",
                "Full Name",
                "e@mail.png",
                "password",
                "password");

        testAddSignUpUserDto(signUpUserDto, true, EmailIsTakenException.class);
    }

    @Test
    void testAddSignUpUserDtoWhenEmailAndLoginAreTaken() {
        Mockito.when(userRepository.existsByLogin("login")).thenReturn(true);
        Mockito.when(userRepository.existsByEmail("e@mail.com")).thenReturn(true);
        Mockito.when(userRepository.existsByLoginOrEmail("login", "e@mail.png")).thenReturn(true);

        SignUpUserDto signUpUserDto = new SignUpUserDto(
                "login",
                "Full Name",
                "e@mail.png",
                "password",
                "password");

        testAddSignUpUserDto(signUpUserDto, true, LoginIsTakenException.class);
    }

    @Test
    void testDeleteUser() {
        when(userRepository.deleteUserByLogin("login"))
                .thenReturn(true);

        boolean actual = userService.deleteUserByLogin("login");

        assertTrue(actual);
        verify(userRepository).deleteUserByLogin("login");
    }

    @Test
    void testDeleteUserWithFailure() {
        when(userRepository.deleteUserByLogin("login"))
                .thenReturn(false);

        boolean actual = userService.deleteUserByLogin("login");

        assertFalse(actual);
        verify(userRepository).deleteUserByLogin("login");
    }

    User getRandUser() {
        return new User(UUID.randomUUID(),
                null, null, null, null, Role.USER);
    }
}