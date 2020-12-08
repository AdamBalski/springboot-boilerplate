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
    void testAddUser() {
        User user = getRandUser();
        when(userRepository.addUser(user))
                .thenReturn(true);

        assertTrue(userService.addUser(user));
        verify(userRepository).addUser(user);
    }

    @Test
    void testAddUserNotSuccessfully() {
        User user = getRandUser();
        when(userRepository.addUser(user))
                .thenReturn(false);

        assertFalse(userService.addUser(user));
        verify(userRepository).addUser(user);
    }

    @Test
    void testGetUserDataByLogin() {
        String login = "user";
        Optional<User> userOptional = Optional.of(getRandUser());

        when(userRepository.getByLogin(login))
                .thenReturn(userOptional);

        assertEquals(userOptional, userService.getUserByLogin(login));
    }


    @Test
    void testGetUserDataByLoginWhenThatLoginDoesNotExist() {
        String login = "user";
        when(userRepository.getByLogin(login))
                .thenReturn(Optional.empty());

        assertEquals(Optional.empty(), userService.getUserByLogin(login));
    }

    @Test
    void testGetUserDataByUUID() {
        UUID uuid = UUID.randomUUID();
        Optional<User> userOptional = Optional.of(getRandUser());

        when(userRepository.getByUUID(uuid))
                .thenReturn(userOptional);

        assertEquals(userOptional, userService.getUserByUUID(uuid));
    }

    @Test
    void testGetUserDataByUUIDWhenThatUuidDoesNotExist() {
        UUID uuid = UUID.randomUUID();
        when(userRepository.getByUUID(uuid))
                .thenReturn(Optional.empty());

        assertEquals(Optional.empty(), userService.getUserByUUID(uuid));
    }

    void testAddSignUpUserDto(SignUpUserDto signUpUserDto,
                              boolean expectedResult,
                              boolean shouldThrowResponseStatusException,
                              String expectedReason) {
        Executable executable = () -> assertEquals(expectedResult, userService.addSignUpUserDto(signUpUserDto));

        if(shouldThrowResponseStatusException) {
            // assert that exception was thrown and get the exception
            var responseStatusException = assertThrows(ResponseStatusException.class, executable);
            assertEquals(expectedReason, responseStatusException.getReason());
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

        testAddSignUpUserDto(signUpUserDto, true, false, null);
    }

    @Test
    void testAddSignUpUserDtoWhenLoginIsInvalid() {
        SignUpUserDto signUpUserDto = new SignUpUserDto(
                "",
                "Full Name",
                "e@mail.png",
                "password",
                "password");

        testAddSignUpUserDto(signUpUserDto, false, true, "SOME_FIELD_IS_INCORRECT");
    }

    @Test
    void testAddSignUpUserDtoWhenFullNameIsInvalid() {
        SignUpUserDto signUpUserDto = new SignUpUserDto(
                "login",
                "@",
                "e@mail.png",
                "password",
                "password");

        testAddSignUpUserDto(signUpUserDto, false, true, "SOME_FIELD_IS_INCORRECT");
    }

    @Test
    void testAddSignUpUserDtoWhenLoginAndFullNameAreInvalid() {
        SignUpUserDto signUpUserDto = new SignUpUserDto(
                "",
                "@",
                "e@mail.png",
                "password",
                "password");

        testAddSignUpUserDto(signUpUserDto, false, true, "SOME_FIELD_IS_INCORRECT");
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

        testAddSignUpUserDto(signUpUserDto, false, true, "LOGIN_IS_TAKEN");    }

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

        testAddSignUpUserDto(signUpUserDto, false, true, "EMAIL_IS_TAKEN");
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

        testAddSignUpUserDto(signUpUserDto, false, true, "LOGIN_IS_TAKEN");
    }

    @Test
    void testDeleteUser() {
        when(userRepository.deleteUser("login"))
                .thenReturn(true);

        boolean actual = userService.deleteUser("login");

        assertTrue(actual);
        verify(userRepository).deleteUser("login");
    }

    @Test
    void testDeleteUserWithFailure() {
        when(userRepository.deleteUser("login"))
                .thenReturn(false);

        boolean actual = userService.deleteUser("login");

        assertFalse(actual);
        verify(userRepository).deleteUser("login");
    }

    User getRandUser() {
        return new User(UUID.randomUUID(),
                null, null, null, null, Role.USER);
    }
}