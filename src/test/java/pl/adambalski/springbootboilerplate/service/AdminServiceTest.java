package pl.adambalski.springbootboilerplate.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pl.adambalski.springbootboilerplate.exception.NoSuchUserException;
import pl.adambalski.springbootboilerplate.model.Role;
import pl.adambalski.springbootboilerplate.model.User;
import pl.adambalski.springbootboilerplate.repository.AdminJpaRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AdminServiceTest {
    AutoCloseable autoCloseable;

    @InjectMocks
    AdminService adminService;

    @Mock
    AdminJpaRepository adminJpaRepository;

    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void destroy() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testGetUserDataByUuidWhenThereIsNoSuchUser() {
        UUID uuid = UUID.randomUUID();
        Mockito.when(adminJpaRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        Executable executable = () -> adminService.getUserByUUID(uuid);
        assertThrows(NoSuchUserException.class, executable);

        Mockito.verify(adminJpaRepository).findByUuid(uuid);
    }

    @Test
    void testGetUserDataByUuidWhenThereIsSuchUser() {
        User expectedUser = getRandUser();
        Optional<User> optionalUser = Optional.of(expectedUser);
        UUID  uuid= UUID.randomUUID();
        Mockito.when(adminJpaRepository.findByUuid(uuid)).thenReturn(optionalUser);

        User actualUser = adminService.getUserByUUID(uuid);
        assertEquals(expectedUser, actualUser);

        Mockito.verify(adminJpaRepository).findByUuid(uuid);
    }

    @Test
    void testGetUserDataByLoginWhenThereIsNoSuchUser() {
        String login = "login";
        Mockito.when(adminJpaRepository.findByLogin(login)).thenReturn(Optional.empty());

        Executable executable = () -> adminService.getUserByLogin(login);
        assertThrows(NoSuchUserException.class, executable);

        Mockito.verify(adminJpaRepository).findByLogin(login);
    }

    @Test
    void testGetUserDataByLoginWhenThereIsSuchUser() {
        User expectedUser = getRandUser();
        Optional<User> optionalUser = Optional.of(expectedUser);
        String login = "login";
        Mockito.when(adminJpaRepository.findByLogin(login)).thenReturn(optionalUser);

        User actualUser = adminService.getUserByLogin(login);
        assertEquals(expectedUser, actualUser);

        Mockito.verify(adminJpaRepository).findByLogin(login);
    }

    @Test
    void testDeleteUserByLoginSuccessfully() {
        String login = "login";
        Mockito.when(adminJpaRepository.deleteUserByLogin(login)).thenReturn(true);

        Executable executable = () -> adminService.deleteUserByLogin(login);
        assertDoesNotThrow(executable);

        Mockito.verify(adminJpaRepository).deleteUserByLogin(login);
    }

    @Test
    void testDeleteUserByLoginUnsuccessfully() {
        String login = "login";
        Mockito.when(adminJpaRepository.deleteUserByLogin(login)).thenReturn(false);

        Executable executable = () -> adminService.deleteUserByLogin(login);
        assertThrows(NoSuchUserException.class, executable);

        Mockito.verify(adminJpaRepository).deleteUserByLogin(login);
    }

        private User getRandUser() {
        return new User(
                UUID.randomUUID(),
                "login",
                "Full Name",
                "e@mail.png",
                "hashed_password",
                Role.USER
        );
    }
}
