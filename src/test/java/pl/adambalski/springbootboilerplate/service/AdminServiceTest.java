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
        Mockito.when(adminRepository.getUserDataByUUID(uuid)).thenReturn(Optional.empty());

        Executable executable = () -> adminService.getUserDataByUUID(uuid);
        assertThrows(NoSuchUserException.class, executable);

        Mockito.verify(adminRepository).getUserDataByUUID(uuid);
    }

    @Test
    void testGetUserDataByUuidWhenThereIsSuchUser() {
        User expectedUser = getRandUser();
        Optional<User> optionalUser = Optional.of(expectedUser);
        UUID  uuid= UUID.randomUUID();
        Mockito.when(adminRepository.getUserDataByUUID(uuid)).thenReturn(optionalUser);

        User actualUser = adminService.getUserDataByUUID(uuid);
        assertEquals(expectedUser, actualUser);

        Mockito.verify(adminRepository).getUserDataByUUID(uuid);
    }

    @Test
    void testGetUserDataByLoginWhenThereIsNoSuchUser() {
        String login = "login";
        Mockito.when(adminRepository.getUserDataByLogin(login)).thenReturn(Optional.empty());

        Executable executable = () -> adminService.getUserDataByLogin(login);
        assertThrows(NoSuchUserException.class, executable);

        Mockito.verify(adminRepository).getUserDataByLogin(login);
    }

    @Test
    void testGetUserDataByLoginWhenThereIsSuchUser() {
        User expectedUser = getRandUser();
        Optional<User> optionalUser = Optional.of(expectedUser);
        String login = "login";
        Mockito.when(adminRepository.getUserDataByLogin(login)).thenReturn(optionalUser);

        User actualUser = adminService.getUserDataByLogin(login);
        assertEquals(expectedUser, actualUser);

        Mockito.verify(adminRepository).getUserDataByLogin(login);
    }

    @Test
    void testDeleteUserByLoginSuccessfully() {
        String login = "login";
        Mockito.when(adminRepository.deleteUserByLogin(login)).thenReturn(true);

        assertTrue(adminService.deleteUserByLogin(login));

        Mockito.verify(adminRepository).deleteUserByLogin(login);
    }

    @Test
    void testDeleteUserByLoginUnsuccessfully() {
        String login = "login";
        Mockito.when(adminRepository.deleteUserByLogin(login)).thenReturn(false);

        assertFalse(adminService.deleteUserByLogin(login));

        Mockito.verify(adminRepository).deleteUserByLogin(login);
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
