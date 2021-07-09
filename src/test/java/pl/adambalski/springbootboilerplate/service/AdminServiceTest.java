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
import pl.adambalski.springbootboilerplate.repository.AdminRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AdminServiceTest {
    AutoCloseable autoCloseable;

    @InjectMocks
    AdminService adminService;

    @Mock
    AdminRepository adminRepository;

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
        Mockito.when(adminRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        Executable executable = () -> adminService.getUserByUUID(uuid);
        assertThrows(NoSuchUserException.class, executable);

        Mockito.verify(adminRepository).findByUuid(uuid);
    }

    @Test
    void testGetUserDataByUuidWhenThereIsSuchUser() {
        User expectedUser = getRandUser();
        Optional<User> optionalUser = Optional.of(expectedUser);
        UUID  uuid= UUID.randomUUID();
        Mockito.when(adminRepository.findByUuid(uuid)).thenReturn(optionalUser);

        User actualUser = adminService.getUserByUUID(uuid);
        assertEquals(expectedUser, actualUser);

        Mockito.verify(adminRepository).findByUuid(uuid);
    }

    @Test
    void testGetUserDataByLoginWhenThereIsNoSuchUser() {
        String login = "login";
        Mockito.when(adminRepository.findByLogin(login)).thenReturn(Optional.empty());

        Executable executable = () -> adminService.getUserByLogin(login);
        assertThrows(NoSuchUserException.class, executable);

        Mockito.verify(adminRepository).findByLogin(login);
    }

    @Test
    void testGetUserDataByLoginWhenThereIsSuchUser() {
        User expectedUser = getRandUser();
        Optional<User> optionalUser = Optional.of(expectedUser);
        String login = "login";
        Mockito.when(adminRepository.findByLogin(login)).thenReturn(optionalUser);

        User actualUser = adminService.getUserByLogin(login);
        assertEquals(expectedUser, actualUser);

        Mockito.verify(adminRepository).findByLogin(login);
    }

    @Test
    void testDeleteUserByLoginSuccessfully() {
        String login = "login";
        Mockito.when(adminRepository.deleteByLogin(login)).thenReturn(1);

        Executable executable = () -> adminService.deleteByLogin(login);
        assertDoesNotThrow(executable);

        Mockito.verify(adminRepository).deleteByLogin(login);
    }

    @Test
    void testDeleteUserByLoginUnsuccessfully() {
        String login = "login";
        Mockito.when(adminRepository.deleteByLogin(login)).thenReturn(0);

        Executable executable = () -> adminService.deleteByLogin(login);
        assertThrows(NoSuchUserException.class, executable);

        Mockito.verify(adminRepository).deleteByLogin(login);
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
