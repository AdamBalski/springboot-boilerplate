package pl.adambalski.springbootboilerplate.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.adambalski.springbootboilerplate.model.Role;
import pl.adambalski.springbootboilerplate.model.User;
import pl.adambalski.springbootboilerplate.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
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

    @BeforeEach
    void init() {
        this.autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void destroy() throws Exception {
        this.autoCloseable.close();
    }

    @Test
    void testDoesUserWithEmailOrLoginExistWhenItDoes() {
        when(userRepository.doesUserWithEmailOrLoginExist("email", "login"))
                .thenReturn(true);

        assertTrue(userService.doesUserWithEmailOrLoginExist("email", "login"));
        verify(userRepository).doesUserWithEmailOrLoginExist("email", "login");
    }

    @Test
    void testDoesUserWithEmailOrLoginExistWhenItDoesNot() {
        when(userRepository.doesUserWithEmailOrLoginExist("email", "login"))
                .thenReturn(false);

        assertFalse(userService.doesUserWithEmailOrLoginExist("email", "login"));
        verify(userRepository).doesUserWithEmailOrLoginExist("email", "login");
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
    void testUnbanEmail() {
        when(userRepository.unbanEmail("email"))
                .thenReturn(true);

        assertTrue(userService.unbanEmail("email"));
        verify(userRepository).unbanEmail("email");
    }

    @Test
    void testUnbanEmailNotSuccessfully() {
        when(userRepository.unbanEmail("email"))
                .thenReturn(false);

        assertFalse(userService.unbanEmail("email"));
        verify(userRepository).unbanEmail("email");
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

    @Test
    void testGet10UsersStartingFromNth() {
        List<User> users = new ArrayList<>(10);
        for(int i = 0; i < 10; i++) {
            users.add(getRandUser());
        }

        when(userRepository.get10UsersStartingFromNth(30)).thenReturn(users);
        assertEquals(users, userService.get10UsersStartingFromNth(30));
    }

    @Test
    void testDeleteUserAndBanEmail() {
        when(userRepository.deleteUser("login"))
                .thenReturn(true);
        when(userRepository.banEmail("email"))
                .thenReturn(true);

        boolean actual = userService.deleteUserAndBanEmail("login", "email");

        assertTrue(actual);
        verify(userRepository).deleteUser("login");
        verify(userRepository).banEmail("email");
    }

    @Test
    void testDeleteUserAndBanEmailWhenBanningEmailHaveNotSucceeded() {
        when(userRepository.deleteUser("login"))
                .thenReturn(true);
        when(userRepository.banEmail("email"))
                .thenReturn(false);

        boolean actual = userService.deleteUserAndBanEmail("login", "email");

        assertFalse(actual);
        verify(userRepository).deleteUser("login");
        verify(userRepository).banEmail("email");
    }

    @Test
    void testDeleteUserAndBanEmailWhenDeletingUserHaveNotSucceeded() {
        when(userRepository.deleteUser("login"))
                .thenReturn(false);
        when(userRepository.banEmail("email"))
                .thenReturn(true);

        boolean actual = userService.deleteUserAndBanEmail("login", "email");

        assertFalse(actual);
        verify(userRepository).deleteUser("login");
        // Short circuit evaluation makes userRepository.banEmail(email) not to execute
        verify(userRepository, never()).banEmail("email");
    }

    @Test
    void testDeleteUserAndBanEmailWhenItHaveNotHaveNotSucceeded() {
        when(userRepository.deleteUser("login"))
                .thenReturn(true);
        when(userRepository.banEmail("email"))
                .thenReturn(false);

        boolean actual = userService.deleteUserAndBanEmail("login", "email");

        assertFalse(actual);
        verify(userRepository).deleteUser("login");
        verify(userRepository).banEmail("email");
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